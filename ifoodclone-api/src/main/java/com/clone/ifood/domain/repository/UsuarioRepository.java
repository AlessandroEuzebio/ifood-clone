package com.clone.ifood.domain.repository;

import java.util.Optional;

import com.clone.ifood.domain.model.Usuario;

public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);
}
