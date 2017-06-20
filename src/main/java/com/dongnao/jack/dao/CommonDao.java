package com.dongnao.jack.dao;

import java.util.List;
import java.util.Map;

import com.dongnao.jack.annotation.Repository;
import com.dongnao.jack.bean.ConsultContent;

@Repository
public interface CommonDao {
    List<ConsultContent> queryConsultContent(Map<?, ?> map);
}
