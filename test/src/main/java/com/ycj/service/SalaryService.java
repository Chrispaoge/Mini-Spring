package com.ycj.service;

import com.ycj.beans.Bean;

@Bean
public class SalaryService {
    public Integer calSalary(Integer experience){
        return experience * 5000 ;
    }
}
