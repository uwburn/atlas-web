package it.mgt.atlas.rpc.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.transaction.Transactional;

import it.mgt.util.json2jpa.JpaDeserializerQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mgt.atlas.entity.Example;
import it.mgt.atlas.entity.Operation;
import it.mgt.util.json2jpa.JpaDeserializer;
import it.mgt.util.spring.web.auth.RequiredOperation;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/example")
@Controller
public class ExampleCtrl {
    
    public static class Msg {

        private Example exampleById;
        private Example exampleByCode;
        private List<Example> examplesByQuery = new ArrayList<>();

        @JsonDeserialize(using = JpaDeserializer.class)
        public Example getExampleById() {
            return exampleById;
        }

        public void setExampleById(Example exampleById) {
            this.exampleById = exampleById;
        }

        @JsonDeserialize(using = JpaDeserializer.class)
        @JpaDeserializerQuery("Example.findByCode")
        public Example getExampleByCode() {
            return exampleByCode;
        }

        public void setExampleByCode(Example exampleByCode) {
            this.exampleByCode = exampleByCode;
        }

        @JsonDeserialize(using = JpaDeserializer.class)
        @JpaDeserializerQuery("Example.findByCode")
        public List<Example> getExamplesByQuery() {
            return examplesByQuery;
        }

        public void setExamplesByQuery(List<Example> examplesByQuery) {
            this.examplesByQuery = examplesByQuery;
        }
        
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @RequiredOperation(Operation.READ_EXAMPLE)
    @Transactional
    public Msg rpc(@RequestBody Msg msg) {
        return msg;
    }

}
