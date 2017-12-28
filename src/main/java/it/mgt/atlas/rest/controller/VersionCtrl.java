package it.mgt.atlas.rest.controller;

import it.mgt.util.spring.version.Version;
import it.mgt.util.spring.version.VersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/version")
@Controller
public class VersionCtrl {

    @Autowired
    private VersionServiceImpl versionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Version get() {
        return new Version(versionService.getMajorVersionNumber(),
                versionService.getMinorVersionNumber(),
                versionService.getMaintenanceVersionNumber());
    }

}
