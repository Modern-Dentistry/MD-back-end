package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.service.OperationTypeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operation-type-items")
@RequiredArgsConstructor
public class OperationTypeItemController {
    private final OperationTypeItemService operationTypeItemService;

//    @PostMapping("/create")
//    public ResponseEntity<Void> create(@Valid @RequestBody OperationTypeItemCreateRequest request) {
//        operationTypeItemService.create(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/read")
//    public ResponseEntity<PageResponse<OperationTypeItem>> read(PageCriteria pageCriteria) {
//        return ResponseEntity.ok(operationTypeItemService.read(pageCriteria));
//    }
//
//    @GetMapping("/read-by-id/{id}")
//    public ResponseEntity<OperationTypeItemReadResponse> readById(@PathVariable Long id) {
//        return ResponseEntity.ok(operationTypeItemService.readById(id));
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody OperationTypeItemUpdateRequest request) {
//        operationTypeItemService.update(id, request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/update/status/{id}")
//    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
//        operationTypeItemService.updateStatus(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        operationTypeItemService.delete(id);
//        return ResponseEntity.status(NO_CONTENT).build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<PageResponse<OperationType>> search(OperationTypeItemSearchRequest request, PageCriteria criteria) {
//        return ResponseEntity.ok(operationTypeItemService.search(request, criteria));
//    }
//
//    @GetMapping("/export/excel")
//    public ResponseEntity<InputStreamResource> exportToExcel() {
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operation_type_items.xlsx")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(operationTypeItemService.exportReservationsToExcel());
//    }
}
