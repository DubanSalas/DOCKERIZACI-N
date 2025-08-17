package pe.edu.vallegrande.project.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.project.dto.DashboardSummaryDTO;
import pe.edu.vallegrande.project.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Opcional, solo si necesitas CORS habilitado para cualquier origen
public class DashboardRest {

    private final DashboardService dashboardService;

    /**
     * Endpoint para obtener el resumen del dashboard con métricas principales.
     * @return DashboardSummaryDTO con datos agregados para mostrar en el dashboard
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }
}
