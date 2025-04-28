package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.PatientRecipeRepository;
import com.rustam.modern_dentistry.dto.request.create.PatRecipeCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatRecipeUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatRecipeReadRes;
import com.rustam.modern_dentistry.mapper.patient_info.PatientRecipeMapper;
import com.rustam.modern_dentistry.service.settings.recipes.RecipeService;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientRecipeService {
    private final UtilService utilService;
    private final RecipeService recipeService;
    private final PatientRecipeMapper patientRecipeMapper;
    private final PatientRecipeRepository patientRecipeRepository;

    public void create(PatRecipeCreateReq req) {
        var patient = utilService.findByPatientId(req.getPatientId());
        var recipe = recipeService.getRecipeById(req.getRecipeId());
        var patientRecipe = patientRecipeMapper.toEntity(req);
        patientRecipe.setPatient(patient);
        patientRecipe.setRecipe(recipe);
        patientRecipeRepository.save(patientRecipe);
    }

    public List<PatRecipeReadRes> read(Long patientId) {
//        var recipes = patientRecipeRepository.findByPatient_Id(patientId);
//        return recipes.stream()
//                .map(patientRecipeMapper::toDto)
//                .toList();
        return null;
    }

    public List<PatRecipeReadRes> readAllById(Long patientId) {
        return null;
    }

    public void update(Long id, @Valid PatRecipeUpdateReq request) {
    }

    public void delete(Long id) {

    }
}
