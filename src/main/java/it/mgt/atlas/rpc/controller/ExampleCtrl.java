package it.mgt.atlas.rpc.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mgt.atlas.entity.Example;
import it.mgt.jpa.json2jpa.JpaDeserializer;
import it.mgt.jpa.json2jpa.JpaDeserializerQuery;
import it.mgt.jpa.json2jpa.JpaDeserializerQueryParam;
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
        @JpaDeserializerQuery(query = "Example.findByCode", params = {
            @JpaDeserializerQueryParam(name = "code", type = String.class)
        })
        public Example getExampleByCode() {
            return exampleByCode;
        }

        public void setExampleByCode(Example exampleByCode) {
            this.exampleByCode = exampleByCode;
        }

        @JsonDeserialize(using = JpaDeserializer.class)
        @JpaDeserializerQuery(query = "Example.findByCode", params = {
            @JpaDeserializerQueryParam(name = "code", type = String.class)
        })
        public List<Example> getExamplesByQuery() {
            return examplesByQuery;
        }

        public void setExamplesByQuery(List<Example> examplesByQuery) {
            this.examplesByQuery = examplesByQuery;
        }
        
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Msg rpc(@RequestBody Msg msg) {
        return msg;
    }

}
