package employees.controller;

import employees.service.CreateEmployeeCommand;
import employees.service.EmployeeDto;
import employees.service.EmployeesService;
import employees.service.UpdateEmployeeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Operations on employees")
public class EmployeesController {

    private EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @GetMapping
    @Operation(summary = "List employees")
    public List<EmployeeDto> listEmployees() {
        return employeesService.listEmployees();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find employee by id")
    public EmployeeDto findEmployeeById(@PathVariable("id") long id) {
        return employeesService.findEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates an employee")
    @ApiResponse(responseCode = "201", description = "employee has been created")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeCommand command, UriComponentsBuilder uri) {
        EmployeeDto employeeDto = employeesService.createEmployee(command);
        return ResponseEntity.created(uri.path("/api/employees/{id}").buildAndExpand(employeeDto.getId()).toUri()).body(employeeDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an employee")
    public EmployeeDto updateEmployee(@PathVariable("id") long id, @RequestBody UpdateEmployeeCommand command) {
        return employeesService.updateEmployee(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an employee")
    public void deleteEmployee(@PathVariable("id") long id) {
        employeesService.deleteEmployee(id);
    }

}
