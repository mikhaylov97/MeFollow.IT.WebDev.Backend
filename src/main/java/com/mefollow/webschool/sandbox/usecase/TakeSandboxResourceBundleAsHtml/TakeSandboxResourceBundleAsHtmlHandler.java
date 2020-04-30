package com.mefollow.webschool.sandbox.usecase.TakeSandboxResourceBundleAsHtml;

import com.mefollow.webschool.sandbox.domain.base.SandboxResource;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceBundleRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static com.mefollow.webschool.sandbox.domain.base.SandboxResourceType.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class TakeSandboxResourceBundleAsHtmlHandler {

    private static final String BASE_TEMPLATE_PATH = "template/";
    private static final String TEMPLATE_PATH_WITH_HTML_ONLY = BASE_TEMPLATE_PATH + "iframe-template-with-html.html";
    private static final String TEMPLATE_PATH_WITH_HTML_AND_CSS = BASE_TEMPLATE_PATH + "iframe-template-with-html-and-css.html";
    private static final String TEMPLATE_PATH_WITH_HTML_AND_JS = BASE_TEMPLATE_PATH + "iframe-template-with-html-and-js.html";
    private static final String TEMPLATE_PATH_WITH_HTML_AND_CSS_AND_JS = BASE_TEMPLATE_PATH + "iframe-template-full.html";

    private static final String REPLACEMENT_KEY_FOR_HTML = "iframe-template-html-url";
    private static final String REPLACEMENT_KEY_FOR_CSS = "iframe-template-css-url";
    private static final String REPLACEMENT_KEY_FOR_JS = "iframe-template-js-url";

    private static final String RESOURCE_URL_TEMPLATE = "/sandbox/bundle/resource/";

    private final SandboxResourceRepository sandboxResourceRepository;
    private final SandboxResourceBundleRepository sandboxResourceBundleRepository;

    public TakeSandboxResourceBundleAsHtmlHandler(SandboxResourceRepository sandboxResourceRepository,
                                                  SandboxResourceBundleRepository sandboxResourceBundleRepository) {
        this.sandboxResourceRepository = sandboxResourceRepository;
        this.sandboxResourceBundleRepository = sandboxResourceBundleRepository;
    }

    public Mono<ResponseEntity<byte[]>> handle(final TakeSandboxResourceBundleAsHtmlCommand command) {
        return sandboxResourceBundleRepository.findById(command.getBundleId())
                .switchIfEmpty(error(SANDBOX_RESOURCE_BUNDLE_NOT_FOUND))
                .flatMapMany(bundle -> sandboxResourceRepository.findAllByBundleId(bundle.getId()))
                .collectList()
                .flatMap(resources -> getTemplatePath(resources)
                        .flatMap(templatePath -> fillReplacements(resources)
                                .flatMap(replacements -> fillTemplateWithReplacements(templatePath, replacements))))
                .flatMap(response -> constructHttpHeaders(response)
                        .map(httpHeaders -> new ResponseEntity<>(response, httpHeaders, HttpStatus.OK)));
    }

    private Mono<HttpHeaders> constructHttpHeaders(byte[] content) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentLength(content.length);
        headers.setContentType(MediaType.valueOf("text/html; charset=utf-8"));
        headers.add("X-Frame-Options", "ALLOW-FROM http://localhost:3000");

        return just(headers);
    }

    private Mono<String> getTemplatePath(List<SandboxResource> resources) {
        final var resourcesTypes = resources.stream().map(SandboxResource::getType).collect(toList());
        if (resourcesTypes.size() == 1 && resourcesTypes.contains(HTML)) {
            return just(TEMPLATE_PATH_WITH_HTML_ONLY);
        } else if (resourcesTypes.size() == 2) {
            if (resourcesTypes.containsAll(asList(HTML, CSS))) {
                return just(TEMPLATE_PATH_WITH_HTML_AND_CSS);
            } else if (resourcesTypes.containsAll(asList(HTML, JS))) {
                return just(TEMPLATE_PATH_WITH_HTML_AND_JS);
            }
        } else if (resourcesTypes.size() == 3 && resourcesTypes.containsAll(asList(HTML, CSS, JS))) {
            return just(TEMPLATE_PATH_WITH_HTML_AND_CSS_AND_JS);
        }

        return error(SANDBOX_RESOURCES_INCONSISTENCY);
    }

    private Mono<Map<String, String>> fillReplacements(List<SandboxResource> resources) {
        final var replacements = new HashMap<String, String>();
        for (SandboxResource resource : resources) {
            switch (resource.getType()) {
                case HTML:
//                    replacements.put(REPLACEMENT_KEY_FOR_HTML, RESOURCE_URL_TEMPLATE + resource.getId());
                    replacements.put(REPLACEMENT_KEY_FOR_HTML, resource.getContent());
                    break;
                case CSS:
                    replacements.put(REPLACEMENT_KEY_FOR_CSS, RESOURCE_URL_TEMPLATE + resource.getId());
                    break;
                case JS:
                    replacements.put(REPLACEMENT_KEY_FOR_JS, RESOURCE_URL_TEMPLATE + resource.getId());
                    break;
                default:
                    return error(WRONG_SANDBOX_RESOURCE_TYPE);
            }
        }

        return just(replacements);
    }

    private Mono<byte[]> fillTemplateWithReplacements(String templatePath, Map<String, String> replacement) {
        try (InputStream in = TakeSandboxResourceBundleAsHtmlHandler.class.getClassLoader().getResourceAsStream(templatePath)) {
            final var pattern = Pattern.compile("\\$\\{.*}");
            var template = IOUtils.toString(in, "UTF-8");
            final var matcher = pattern.matcher(template);
            template = matcher.replaceAll(result -> {
                String key = result.group().substring(2, result.group().length() - 1);
                return replacement.containsKey(key) ? replacement.get(key) : "${" + key + "}";
            });

            return just(template.getBytes());
        } catch (IOException e) {
            return error(e);
        }
    }
}
