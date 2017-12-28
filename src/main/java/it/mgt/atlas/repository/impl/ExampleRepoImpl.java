package it.mgt.atlas.repository.impl;

import it.mgt.atlas.entity.Example;
import org.springframework.stereotype.Repository;
import it.mgt.atlas.repository.ExampleRepo;
import it.mgt.util.spring.repository.BaseRepositoryImpl;

@Repository
public class ExampleRepoImpl extends BaseRepositoryImpl<Example, Long> implements ExampleRepo {

    public ExampleRepoImpl() {
        super(Example.class);
    }

    @Override
    public Long getKey(Example entity) {
        return entity.getId();
    }

    @Override
    public void setKey(Example example, Long key) {
        example.setId(key);
    }
}
