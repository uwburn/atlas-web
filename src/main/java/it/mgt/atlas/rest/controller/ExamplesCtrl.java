package it.mgt.atlas.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import it.mgt.atlas.annotation.CustomExampleInj;
import it.mgt.atlas.entity.Example;
import it.mgt.atlas.entity.Operation;
import it.mgt.atlas.view.DefaultView;
import it.mgt.util.spring.web.jsonview.DynamicJsonView;
import it.mgt.util.spring.web.resolver.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import it.mgt.atlas.repository.ExampleRepo;
import it.mgt.util.json2jpa.Json2JpaFactory;
import it.mgt.util.spring.web.auth.RequiredOperation;
import it.mgt.util.spring.web.jpa.JpaInj;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/examples")
@Controller
public class ExamplesCtrl {

    @Autowired
    private ExampleRepo exampleRepo;

    @Autowired
    private Json2JpaFactory json2JpaFactory;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public List<Example> get(@JpaInj(
            defaultQuery = "Example.findAll"
    ) List<Example> examples) {
        return examples;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public Example getById(@JpaInj(
            primaryKey = "id",
            pathParams = {
                @PathParam(name = "id", path = "id")
            }
    ) Example example) {
        return example;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/first")
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public Example getFirst(@JpaInj Example example) {
        return example;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/count")
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    public Long count(@JpaInj(
            query = "Example.countAll"
    ) Long count) {
        return count;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/customResolver")
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public Example getCustomResolver(@CustomExampleInj Example example) {
        return example;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @RequiredOperation(Operation.ADD_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public Example post(@RequestBody JsonNode json) {
        Example example = json2JpaFactory.build()
                .setMaxDepth(1)
                .construct(Example.class, json);

        return example;
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    @ResponseBody
    @Transactional
    @RequiredOperation(Operation.EDIT_EXAMPLE)
    @DynamicJsonView(defaultView = DefaultView.class)
    public Example patch(@JpaInj(
            primaryKey = "id",
            pathParams = {
                @PathParam(name = "id", path = "id")
            }
    ) Example creationReason, JsonNode json) {
        return json2JpaFactory.build()
                .setMaxDepth(1)
                .merge(creationReason, json);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @RequiredOperation(Operation.REMOVE_EXAMPLE)
    public void delete(@JpaInj(
            primaryKey = "id",
            pathParams = {
                @PathParam(name = "id", path = "id")
            }
    ) Example creationReason) {
    	exampleRepo.remove(creationReason);
    }

}
