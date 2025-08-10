package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.WorkersWorkSchedule;
import com.rustam.modern_dentistry.dao.entity.settings.Cabinet;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.repository.WorkersWorkScheduleRepository;
import com.rustam.modern_dentistry.dto.request.create.WorkersWorkScheduleRequest;
import com.rustam.modern_dentistry.dto.request.read.WorkersWorkScheduleSearchRequest;
import com.rustam.modern_dentistry.dto.response.read.WorkersWorkScheduleResponse;
import com.rustam.modern_dentistry.dto.response.update.WorkersWorkScheduleUpdateDTO;
import com.rustam.modern_dentistry.exception.custom.WorkersWorkScheduleNotFoundException;
import com.rustam.modern_dentistry.mapper.WorkersWorkScheduleMapper;
import com.rustam.modern_dentistry.service.settings.CabinetService;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.WorkersWorkScheduleSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkersWorkScheduleService {

    WorkersWorkScheduleRepository workersWorkScheduleRepository;
    UtilService utilService;
    WorkersWorkScheduleMapper workersWorkScheduleMapper;
    CabinetService cabinetService;

    public void create(WorkersWorkScheduleRequest workersWorkScheduleRequest) {
        BaseUser baseUser = utilService.findByBaseUserId(workersWorkScheduleRequest.getUserId());
        WorkersWorkSchedule workersWorkSchedule = WorkersWorkSchedule.builder()
                .weekDay(workersWorkScheduleRequest.getWeekDay())
                .cabinet(cabinetService.findByCabinetName(workersWorkScheduleRequest.getCabinetName()))
                .worker(baseUser)
                .startTime(workersWorkScheduleRequest.getStartTime())
                .finishTime(workersWorkScheduleRequest.getFinishTime())
                .build();
        workersWorkScheduleRepository.save(workersWorkSchedule);
    }

    public List<WorkersWorkScheduleResponse> read() {
        return workersWorkScheduleRepository.findAllWorkersWorkSchedule();
    }

    public WorkersWorkSchedule findById(Long id){
        return workersWorkScheduleRepository.findById(id)
                .orElseThrow(() -> new WorkersWorkScheduleNotFoundException("No such work calendar was found."));
    }

    public WorkersWorkScheduleUpdateDTO update(WorkersWorkScheduleUpdateDTO workersWorkScheduleUpdateDTO) {
        WorkersWorkSchedule workersWorkSchedule = findById(workersWorkScheduleUpdateDTO.getId());
        if (workersWorkScheduleUpdateDTO.getCabinetName() != null){
            workersWorkSchedule.setCabinet(cabinetService.findByCabinetName(workersWorkScheduleUpdateDTO.getCabinetName()));
        }
        utilService.updateFieldIfPresent(workersWorkScheduleUpdateDTO.getWeekDay(),workersWorkSchedule::setWeekDay);
        utilService.updateFieldIfPresent(workersWorkScheduleUpdateDTO.getStartTime(),workersWorkSchedule::setStartTime);
        utilService.updateFieldIfPresent(workersWorkScheduleUpdateDTO.getFinishTime(),workersWorkSchedule::setFinishTime);
        workersWorkScheduleRepository.save(workersWorkSchedule);
        return workersWorkScheduleMapper.toDto(workersWorkSchedule);
    }

    public void delete(Long id) {
        WorkersWorkSchedule workersWorkSchedule = findById(id);
        workersWorkScheduleRepository.delete(workersWorkSchedule);
    }

    public List<WorkersWorkScheduleResponse> search(WorkersWorkScheduleSearchRequest workersWorkScheduleSearchRequest) {
        List<WorkersWorkSchedule> workersWorkSchedules = workersWorkScheduleRepository.findAll(WorkersWorkScheduleSpecification.filterBy(workersWorkScheduleSearchRequest.getWeekDay()));
        return workersWorkScheduleMapper.toDtos(workersWorkSchedules);
    }
}
