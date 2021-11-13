package com.qzero.tunnel.client.data.repository;

import com.qzero.tunnel.client.data.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken,String> {
}
