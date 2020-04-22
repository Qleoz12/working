package com.api.service.producto;

import java.util.List;

import com.api.dao.empleado.ProductoEntity;

public interface IProductoService {

	List<ProductoEntity> findAllProductos();

	ProductoEntity findByIdProducto(Long id);

	ProductoEntity saveProducto(ProductoEntity producto);

	void deleteProducto(Long id);

}
