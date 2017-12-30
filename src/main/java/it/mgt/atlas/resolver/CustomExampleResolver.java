package it.mgt.atlas.resolver;

import it.mgt.atlas.annotation.CustomExampleInj;
import it.mgt.atlas.entity.Example;
import it.mgt.atlas.repository.ExampleRepo;
import it.mgt.util.spring.web.config.Resolver;
import it.mgt.util.spring.web.exception.BadRequestException;
import it.mgt.util.spring.web.exception.NotFoundException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Resolver
public class CustomExampleResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private ExampleRepo exampleRepo;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CustomExampleInj.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String[]> queryParams = webRequest.getParameterMap();
        
        String[] idParams = queryParams.get("id");
        
        if (idParams.length == 0)
            throw new BadRequestException();
        
        Example example;
        try {
            Long id = Long.parseLong(idParams[0]);
            example = exampleRepo.find(id);
        }
        catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        
        if (example == null)
            throw new NotFoundException();
        
        return example;
    }

    
    
}
