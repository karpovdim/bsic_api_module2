package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = {"/{giftCertificateId}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags(
            @PathVariable(name = "giftCertificateId", required = false) Long giftCertificateId,
            @RequestParam(name = "allWithTag", required = false) String allWithTag,
            @RequestParam(name = "tagName", required = false) String tagName,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderType,
            @RequestParam(name = "filter", required = false) List<String> filterBy
    ) {
        return giftCertificateService.getRoute(tagName, sortColumns, orderType,
                filterBy, giftCertificateId, allWithTag);
    }

    @PatchMapping(value = "/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("giftCertificateId") Long id,
                                         @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    @DeleteMapping(value = "/{giftCertificateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("giftCertificateId") Long id) {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody GiftCertificateDto giftCertificateDto) {
        giftCertificateService.create(giftCertificateDto);
    }
}
