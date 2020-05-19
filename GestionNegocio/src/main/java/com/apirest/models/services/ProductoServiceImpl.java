package com.api.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.dao.empleado.ProductoEntity;
import com.api.dao.producto.IDaoProducto;

@Service
public class ProductoServiceImpl implements IProductoService{
	
	
	@Autowired
	IDaoProducto DaoProducto;

	@Override
	public List<ProductoEntity> findAllProductos() {
		return DaoProducto.findAll();
	}

	@Override
	public ProductoEntity findByIdProducto(Long id) {
		return DaoProducto.findById(id).orElse(null);
	}

	@Override
	public ProductoEntity saveProducto(ProductoEntity producto) {
		return DaoProducto.save(producto);
	}

	@Override
	public void deleteProducto(Long id) {
		DaoProducto.deleteById(id);
		
	}

}
