package com.qzero.tunnel.client.data.repository;

import com.qzero.tunnel.client.data.ServerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerProfileRepository extends JpaRepository<ServerProfile,String> {

}
