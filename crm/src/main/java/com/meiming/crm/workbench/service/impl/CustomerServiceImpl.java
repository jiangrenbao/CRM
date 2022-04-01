/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-09
 * @公司： www.meiming.com
 * @描述：
 */
package com.meiming.crm.workbench.service.impl;

import com.meiming.crm.workbench.domain.Customer;
import com.meiming.crm.workbench.mapper.CustomerMapper;
import com.meiming.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>NAME: CustomerServiceImpl</p>
 * @author Administrator
 * @date 2020-06-09 15:57:58
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> queryCustomerByName(String name) {
        return customerMapper.selectCustomerByName(name);
    }
}
