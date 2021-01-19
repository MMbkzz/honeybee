package com.stackstech.honeybee.server.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.HttpHeader;
import com.stackstech.honeybee.server.core.enums.TokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class AuthTokenBuilder {

    @Value("${app.security.token.issuer:honeybee}")
    private String issuer;

    @Value("${app.security.token.secret:honeybee}")
    private String secret;

    //@Value("${app.security.token.expires:180000}")
    private final int expires = 10;

    /**
     * JWT algorithm
     */
    private Algorithm algorithm;
    /**
     * disturb factor
     */
    private static final int DISTURB_FACTOR = 18;

    private Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(secret);
        }
        return algorithm;
    }

    private JWTVerifier jwtVerifier() {
        return JWT.require(getAlgorithm()).withIssuer(issuer).build();
    }

    private String distraction(String token, boolean disturb) {
        if (disturb) {
            return StringUtils.join(token, RandomStringUtils.randomAlphanumeric(DISTURB_FACTOR));
        } else {
            return StringUtils.substring(token, 0, token.length() - DISTURB_FACTOR);
        }
    }

    public String generateTokenId() {
        return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
    }


    public String generateToken(AccountEntity account) {
        Date nowTime = DateTime.now().toDate();
        Date expiresTime = DateTime.now().plusSeconds(expires).toDate();
        // create new authentication token
        String token = JWT.create()
                .withJWTId(generateTokenId())
                .withClaim(AccountEntity.ACCOUNT_ID, account.getId())
                .withClaim(AccountEntity.ACCOUNT_NAME, account.getAccountName())
                .withClaim(AccountEntity.ACCOUNT_PWD, account.getAccountPassword())
                .withIssuer(issuer)
                .withIssuedAt(nowTime)
                .withExpiresAt(expiresTime)
                .sign(getAlgorithm());
        // distraction token
        String distraction = distraction(token, true);
        // encode token
        String newToken = CommonUtil.encodeBase64(distraction);
        log.debug("Issue new authentication token {}, create at {}, expires at {}.", newToken, nowTime, expiresTime);
        return newToken;
    }

    public TokenStatus verifyToken(String token) {
        try {
            String src = CommonUtil.decodeBase64(token);
            String real = distraction(src, false);
            jwtVerifier().verify(real);
        } catch (TokenExpiredException e) {
            log.debug("", e);
            return TokenStatus.EXPIRES;
        } catch (JWTVerificationException e1) {
            log.debug("", e1);
            return TokenStatus.INVALID;
        } catch (Exception e2) {
            log.debug("", e2);
            return TokenStatus.INVALID;
        }
        return TokenStatus.VALID;
    }

    public void refreshAuthToken(String currentToken, HttpServletResponse response) {
        // decode current token
        String src = CommonUtil.decodeBase64(currentToken);
        AccountEntity account = new AccountEntity();
        DecodedJWT decodedJWT = JWT.decode(src);
        account.setId(decodedJWT.getClaim(AccountEntity.ACCOUNT_ID).asLong());
        account.setAccountName(decodedJWT.getClaim(AccountEntity.ACCOUNT_NAME).asString());
        account.setAccountPassword(decodedJWT.getClaim(AccountEntity.ACCOUNT_PWD).asString());
        // generate new token
        String token = generateToken(account);
        response.addHeader(HttpHeader.AUTHORIZATION, token);
        response.addHeader(HttpHeader.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeader.AUTHORIZATION);
        response.addHeader(HttpHeader.CACHE_CONTROL, Constant.NO_STORE);
    }

    public String getClaimValue(String token, String key) {
        String src = CommonUtil.decodeBase64(token);
        return JWT.decode(src).getClaim(key).asString();
    }

}