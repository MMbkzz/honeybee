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
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * @author william
 */
@Slf4j
@Component
public class AuthTokenBuilder {

    @Value("${app.security.token.issuer:honeybee}")
    private String issuer;

    @Value("${app.security.token.secret:honeybee}")
    private String secret;

    //@Value("${app.security.token.expires:180}")
    private final int expires = 10;

    @Autowired
    private CacheUtil cacheUtil;

    /**
     * JWT algorithm
     */
    private Algorithm algorithm;

    private Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(secret);
        }
        return algorithm;
    }

    private JWTVerifier jwtVerifier() {
        return JWT.require(getAlgorithm()).withIssuer(issuer).build();
    }


    public String generateTokenId() {
        return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
    }


    public String generateToken(AccountEntity account) {
        Date nowTime = DateTime.now().toDate();
        Date expiresTime = DateTime.now().plusSeconds(expires).toDate();
        String tokenId = generateTokenId();
        // create new authentication token
        String token = JWT.create()
                .withJWTId(tokenId)
                .withClaim(AccountEntity.ACCOUNT_ID, account.getId())
                .withClaim(AccountEntity.ACCOUNT_NAME, account.getAccountName())
                .withClaim(AccountEntity.ACCOUNT_PWD, account.getAccountPassword())
                .withIssuer(issuer)
                .withIssuedAt(nowTime)
                .withExpiresAt(expiresTime)
                .sign(getAlgorithm());
        // encode token
        String newToken = CommonUtil.encodeBase64(token);
        log.info("Issue new authentication token {}, create at {}, expires at {}.", newToken, nowTime, expiresTime);
        return newToken;
    }

    public TokenStatus verifyToken(String token) {
        try {
            String src = CommonUtil.decodeBase64(token);
            if (cacheUtil.hasBlacklist(JWT.decode(src).getId())) {
                return TokenStatus.INVALID;
            }
            jwtVerifier().verify(src);
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
        try {
            // record token id to blacklist
            cacheUtil.addBlacklist(decodedJWT.getId());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public String getClaimValue(String token, String key) {
        String src = CommonUtil.decodeBase64(token);
        return JWT.decode(src).getClaim(key).asString();
    }

}