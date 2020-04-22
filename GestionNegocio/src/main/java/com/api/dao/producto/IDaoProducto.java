package com.api.dao.producto;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.dao.empleado.ProductoEntity;

public interface IDaoProducto extends JpaRepository<ProductoEntity, Long> {
	

}
