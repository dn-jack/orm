package com.dongnao.jack.pool;


public interface IPool {
    
    PooledConnection getConnection();
    
    void createConnections(int count);
}
