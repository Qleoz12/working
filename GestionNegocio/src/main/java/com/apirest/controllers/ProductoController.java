package com.api.controller.producto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dao.empleado.ProductoEntity;
import com.api.service.producto.IProductoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ProductoController {

	@Autowired
	IProductoService productoService;

	@GetMapping("/producto")
	private List<ProductoEntity> listAllProductos() {
		List<ProductoEntity> ListaDeProductos = productoService.findAllProductos();
		// colocar logs
		return ListaDeProductos;
	}

	@GetMapping("/producto/{id}")
	private ResponseEntity<?> findProducto(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		ProductoEntity productoEncontrado = null;

		try {
			productoEncontrado = productoService.findByIdProducto(id);

		} catch (DataAccessException errorDB) {
			response.put("mensajeError", "error al realizar consulta en la base de datos");
			response.put("descripcionError",
					errorDB.getMessage().concat(": ").concat(errorDB.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productoEncontrado == null) {
			response.put("descripcionError",
					"el producto con el id " + ": " + id + " " + "no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		// colocar logs
		response.put("mensajeExito", "El producto ha sido encontrado");
		response.put("producto", productoEncontrado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/producto")
	private ResponseEntity<?> saveProducto(@Valid @RequestBody ProductoEntity producto, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		ProductoEntity productoCreado = null;

		if (result.hasErrors()) {

			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("erroresFormulario", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			productoCreado = productoService.saveProducto(producto);

		} catch (DataAccessException errorDB) {
			response.put("mensajeError", "error al realizar el insert en la base de datos");
			response.put("descripcionError",
					errorDB.getMessage().concat(": ").concat(errorDB.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensajeExito", "El producto ha sido creado");
		response.put("producto", productoCreado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@PutMapping("/producto/{id}")
	private ResponseEntity<?> updateProducto(@Valid @RequestBody ProductoEntity producto, @PathVariable Long id,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		ProductoEntity productoDB = productoService.findByIdProducto(id);
		ProductoEntity productoActualizado = null;

		if (result.hasErrors()) {

			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("erroresFormulario", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (productoDB == null) {

			response.put("descripcionError",
					"el producto con el id " + ": " + id + " " + "no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		}

		try {

			productoDB.setCantidad(producto.getCantidad());
			productoDB.setNombre(producto.getNombre());
			productoDB.setPrecio(producto.getPrecio());
			productoActualizado = productoService.saveProducto(productoDB);

		} catch (DataAccessException errorDB) {
			response.put("mensajeError", "error al realizar la busqueda del producto en la base de datos");
			response.put("descripcionError",
					errorDB.getMessage().concat(": ").concat(errorDB.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensajeExito", "el producto con el id: " + id + " ha sido actualizado con exito");
		response.put("producto", productoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			productoService.deleteProducto(id);

		} catch (DataAccessException errorDB) {
			response.put("mensajeError",
					"error al intentar eliminar el producto con el id: " + id + " en la base de datos");
			response.put("descripcionError",
					errorDB.getMessage().concat(": ").concat(errorDB.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		response.put("mensajeExito", "El producto con el id: " + id + " ha sido eliminado con exito");
		response.put("idProducto", id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		// colocar logs

	}

}
