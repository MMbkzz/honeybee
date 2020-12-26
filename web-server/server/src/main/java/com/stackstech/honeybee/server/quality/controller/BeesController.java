/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.server.quality.entity.Bees;
import com.stackstech.honeybee.server.quality.service.BeesService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class BeesController {
    @Autowired
    private BeesService measureService;

    @RequestMapping(value = "/bees", method = RequestMethod.GET)
    public List<? extends Bees> getAllAliveMeasures(@RequestParam(value =
            "type", defaultValue = "") String type) {
        return measureService.getAllAliveBees(type);
    }

    @RequestMapping(value = "/bees/{id}", method = RequestMethod.GET)
    public Bees getBeesById(@PathVariable("id") long id) {
        return measureService.getBeesById(id);
    }

    @RequestMapping(value = "/bees/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeesById(@PathVariable("id") Long id) throws
            SchedulerException {
        measureService.deleteBeesById(id);
    }

    @RequestMapping(value = "/bees", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBees() throws SchedulerException {
        measureService.deleteBees();
    }

    @RequestMapping(value = "/bees", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Bees updateBees(@RequestBody Bees measure) {
        return measureService.updateBees(measure);
    }

    @RequestMapping(value = "/bees/owner/{owner}", method =
            RequestMethod.GET)
    public List<Bees> getAliveMeasuresByOwner(@PathVariable("owner")
                                              @Valid String owner) {
        return measureService.getAliveBeesByOwner(owner);
    }

    @RequestMapping(value = "/bees", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Bees createBees(@RequestBody Bees measure) {
        return measureService.createBees(measure);
    }
}
