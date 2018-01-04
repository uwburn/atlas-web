package it.mgt.atlas.rest.controller;

import it.mgt.util.spring.meta.ProjectMeta;
import it.mgt.util.spring.meta.ProjectMetaSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/projectMeta")
@Controller
public class ProjectMetaCtrl {

    @Autowired
    private ProjectMetaSvc projectMetaSvc;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ProjectMeta get() {
        return projectMetaSvc.getProjectMeta();
    }

}
