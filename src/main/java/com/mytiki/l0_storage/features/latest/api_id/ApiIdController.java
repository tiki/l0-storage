/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_storage.features.latest.api_id;

import com.mytiki.spring_rest_api.ApiConstants;
import com.mytiki.spring_rest_api.ApiPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "API ID")
@RestController
@RequestMapping(value = ApiIdController.PATH_CONTROLLER)
public class ApiIdController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "api-id";
    public static final String PATH_ID = "/id";
    public static final String PATH_NEW = "/new";
    private final ApiIdService service;

    public ApiIdController(ApiIdService service) {
        this.service = service;
    }

    @Operation(summary = "Get all provisioned API Ids", security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.GET)
    public ApiPage<ApiIdAORsp> getAll(
            Authentication authentication,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size) {
        return service.all(authentication.getName(), page, size);
    }

    @Operation(summary = "Get an API Id's properties", security = @SecurityRequirement(name = "jwt"), responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = ApiIdAORsp.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, path = PATH_ID + "/{api-id}")
    public ApiIdAORsp getKey(
            Authentication authentication,
            @PathVariable(name = "api-id") String apiId){
        return service.get(apiId, authentication.getName());
    }

    @Operation(summary = "Revoke an API Id (permanent)", security = @SecurityRequirement(name = "jwt"), responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = ApiIdAORsp.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_ID + "/{api-id}")
    public ApiIdAORsp deleteKey(
            Authentication authentication,
            @PathVariable(name = "api-id") String apiId){
        return service.revoke(apiId, authentication.getName());
    }

    @Operation(summary = "Request a new API Id", security = @SecurityRequirement(name = "jwt"))
    @RequestMapping(method = RequestMethod.POST, path = PATH_NEW)
    public ApiIdAORsp postNew(Authentication authentication){
        return service.register(authentication.getName());
    }
}
