package com.rustam.modern_dentistry.service.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Medicine;
import com.rustam.modern_dentistry.dao.repository.settings.recipes.MedicineRepository;
import com.rustam.modern_dentistry.dto.request.create.MedicineCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.update.MedicineUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.MedicineReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.settings.recipes.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineMapper medicineMapper;
    private final MedicineRepository medicineRepository;
    private final RecipeService recipeService;

    public void create(MedicineCreateRequest request) {
        var recipe = recipeService.getRecipeById(request.getRecipeId());
        var medicine = medicineMapper.toEntity(request);
        medicine.setRecipe(recipe);
        medicineRepository.save(medicine);
    }

    public PageResponse<MedicineReadResponse> read(Long recipeId, PageCriteria pageCriteria) {
        var medicines = medicineRepository.findByRecipe_Id(
                recipeId, PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        return new PageResponse<>(medicines.getTotalPages(), medicines.getTotalElements(), getContent(medicines.getContent()));
    }

    public MedicineReadResponse readById(Long id) {
        var medicine = getMedicineById(id);
        return medicineMapper.toReadDto(medicine);
    }

    public void update(Long id, MedicineUpdateRequest request) {
    }

    public void updateStatus(Long id) {
    }

    public void delete(Long id) {

    }

    private List<MedicineReadResponse> getContent(List<Medicine> content) {
        return content.stream().map(medicineMapper::toReadDto).toList();
    }

    private Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də dərman tapımadı: " + id)
        );
    }
}
