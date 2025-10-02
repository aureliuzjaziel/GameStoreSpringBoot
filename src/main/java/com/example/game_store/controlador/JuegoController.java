package com.example.game_store.controlador;

import com.example.game_store.entidad.Categoria;
import com.example.game_store.entidad.Juego;
import com.example.game_store.repositorio.CategoriaRepository;
import com.example.game_store.repositorio.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/juegos")
@CrossOrigin(origins = "http://localhost:4200")
public class JuegoController {@Autowired
private JuegoRepository juegoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Directorio base para uploads
    private final String UPLOAD_BASE_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "juegos";

    // === ENDPOINTS EXISTENTES ===
    @GetMapping
    public List<Juego> getAllJuegos() {
        return juegoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> getJuegoById(@PathVariable Long id) {
        Optional<Juego> juego = juegoRepository.findById(id);
        return juego.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Juego createJuego(@RequestBody Juego juego) {
        return juegoRepository.save(juego);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Juego> updateJuego(@PathVariable Long id, @RequestBody Juego juegoDetails) {
        Optional<Juego> juego = juegoRepository.findById(id);
        if (juego.isPresent()) {
            Juego juegoToUpdate = juego.get();
            juegoToUpdate.setNombre(juegoDetails.getNombre());
            juegoToUpdate.setDescripcion(juegoDetails.getDescripcion());
            juegoToUpdate.setPrecio(juegoDetails.getPrecio());
            juegoToUpdate.setImagen(juegoDetails.getImagen());
            juegoToUpdate.setStock(juegoDetails.getStock());
            juegoToUpdate.setDesarrollador(juegoDetails.getDesarrollador());
            juegoToUpdate.setPlataforma(juegoDetails.getPlataforma());
            juegoToUpdate.setCategoria(juegoDetails.getCategoria());
            return ResponseEntity.ok(juegoRepository.save(juegoToUpdate));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJuego(@PathVariable Long id) {
        if (juegoRepository.existsById(id)) {
            juegoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === ENDPOINTS PARA SUBIDA DE ARCHIVOS (COMPATIBLES CON TU ANGULAR) ===
    @PostMapping("/upload")
    public ResponseEntity<?> createJuegoWithFile(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precio,
            @RequestParam("stock") String stock,
            @RequestParam("desarrollador") String desarrollador,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("categoriaId") String categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            System.out.println("=== DATOS RECIBIDOS ===");
            System.out.println("Nombre: " + nombre);
            System.out.println("Descripción: " + descripcion);
            System.out.println("Precio: " + precio);
            System.out.println("Stock: " + stock);
            System.out.println("Desarrollador: " + desarrollador);
            System.out.println("Plataforma: " + plataforma);
            System.out.println("CategoriaId: " + categoriaId);
            System.out.println("Imagen: " + (imagen != null ? imagen.getOriginalFilename() : "No imagen"));

            // Buscar la categoría
            Optional<Categoria> categoria = categoriaRepository.findById(Long.parseLong(categoriaId));
            if (!categoria.isPresent()) {
                return ResponseEntity.badRequest().body("Categoría no encontrada");
            }

            // Crear el juego
            Juego juego = new Juego();
            juego.setNombre(nombre);
            juego.setDescripcion(descripcion);
            juego.setPrecio(new BigDecimal(precio));
            juego.setStock(Integer.parseInt(stock));
            juego.setDesarrollador(desarrollador);
            juego.setPlataforma(plataforma);
            juego.setCategoria(categoria.get());

            // Procesar imagen si se envió
            if (imagen != null && !imagen.isEmpty()) {
                System.out.println("Procesando imagen: " + imagen.getOriginalFilename());
                String imagePath = saveImageFile(imagen);
                juego.setImagen(imagePath);
                System.out.println("Imagen guardada en: " + imagePath);
            }

            Juego savedJuego = juegoRepository.save(juego);
            System.out.println("Juego guardado con ID: " + savedJuego.getId());

            return ResponseEntity.ok(savedJuego);

        } catch (Exception e) {
            System.err.println("Error al crear juego: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/upload")
    public ResponseEntity<?> updateJuegoWithFile(
            @RequestParam("id") String id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precio,
            @RequestParam("stock") String stock,
            @RequestParam("desarrollador") String desarrollador,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("categoriaId") String categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            Optional<Juego> optionalJuego = juegoRepository.findById(Long.parseLong(id));
            if (!optionalJuego.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Optional<Categoria> categoria = categoriaRepository.findById(Long.parseLong(categoriaId));
            if (!categoria.isPresent()) {
                return ResponseEntity.badRequest().body("Categoría no encontrada");
            }

            Juego juego = optionalJuego.get();

            // Si se envía una nueva imagen, eliminar la anterior y guardar la nueva
            if (imagen != null && !imagen.isEmpty()) {
                // Eliminar imagen anterior si existe
                if (juego.getImagen() != null && !juego.getImagen().isEmpty()) {
                    deleteImageFile(juego.getImagen());
                }

                // Guardar nueva imagen
                String imagePath = saveImageFile(imagen);
                juego.setImagen(imagePath);
            }

            // Actualizar otros campos
            juego.setNombre(nombre);
            juego.setDescripcion(descripcion);
            juego.setPrecio(new BigDecimal(precio));
            juego.setStock(Integer.parseInt(stock));
            juego.setDesarrollador(desarrollador);
            juego.setPlataforma(plataforma);
            juego.setCategoria(categoria.get());

            Juego updatedJuego = juegoRepository.save(juego);
            return ResponseEntity.ok(updatedJuego);

        } catch (Exception e) {
            System.err.println("Error al actualizar juego: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // === MÉTODOS AUXILIARES ===
    private String saveImageFile(MultipartFile file) throws IOException {
        // Crear directorio si no existe
        File uploadDir = new File(UPLOAD_BASE_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("Directorio creado: " + created + " - " + uploadDir.getAbsolutePath());
        }

        // Generar nombre único para el archivo
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.]", "_");

        // Guardar archivo
        File destFile = new File(uploadDir, uniqueFilename);
        file.transferTo(destFile);

        System.out.println("Archivo guardado en: " + destFile.getAbsolutePath());

        // Retornar la ruta relativa para la URL
        return "/uploads/juegos/" + uniqueFilename;
    }

    private void deleteImageFile(String imagePath) {
        try {
            if (imagePath != null && imagePath.startsWith("/uploads/juegos/")) {
                String filename = imagePath.substring("/uploads/juegos/".length());
                File file = new File(UPLOAD_BASE_DIR, filename);
                if (file.exists()) {
                    boolean deleted = file.delete();
                    System.out.println("Imagen anterior eliminada: " + deleted + " - " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
        }
    }

    // === ENDPOINTS ADICIONALES ===
    @GetMapping("/categoria/{categoriaId}")
    public List<Juego> getJuegosByCategoria(@PathVariable Long categoriaId) {
        return juegoRepository.findByCategoriaId(categoriaId);
    }

    @GetMapping("/buscar/{nombre}")
    public List<Juego> buscarJuegosPorNombre(@PathVariable String nombre) {
        return juegoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}

