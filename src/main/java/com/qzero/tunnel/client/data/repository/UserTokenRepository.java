package com.qzero.tunnel.client.data.repository;

import com.qzero.tunnel.client.data.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken,String> {

    List<UserToken> findAllByServerIp(String serverIp);
    void deleteAllByServerIp(String serverIp);

}
