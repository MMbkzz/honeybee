package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.*;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * DataServiceController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataAssetsController {

    private final Logger log = LoggerFactory.getLogger(DataAssetsController.class);

    @Autowired
    private DataService<AssetsModelEntity> dataAssetsService;

    @Autowired
    private DataService<AssetsCatalogEntity> dataAssetsCatalogService;

    @RequestMapping(value = "/data/assets/model/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getModel(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/assets/model/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteModel(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/assets/model/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateModel(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseMap<?> addModel(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/data/assets/model/query", method = RequestMethod.POST)
    public ResponseMap<?> queryModel(@RequestBody RequestParameter parameters) {
        return null;
    }

    @RequestMapping(value = "/data/assets/catalog/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@RequestBody RequestParameter parameters) {
        return null;
    }


}
