package com.hema.examples.multidbsample.models;

import com.hema.examples.multidbsample.controllers.TenantContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by hemabhatia on 2/8/17.
 */
public class MultiTenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }
}
