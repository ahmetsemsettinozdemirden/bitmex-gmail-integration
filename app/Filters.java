import filters.AuditLoggerFilter;
import filters.JwtFilter;
import play.api.http.EnabledFilters;
import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import java.util.List;

public class Filters extends DefaultHttpFilters {

    private final EnabledFilters enabledFilters;
    private final EssentialFilter jwtFilter;
    private final EssentialFilter auditLoggerFilter;

    @Inject
    public Filters(EnabledFilters enabledFilters, JwtFilter jwtFilter, AuditLoggerFilter auditLoggerFilter) {
        this.enabledFilters = enabledFilters;
        this.jwtFilter = jwtFilter;
        this.auditLoggerFilter = auditLoggerFilter;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        List<EssentialFilter> filters = enabledFilters.asJava().getFilters();
        filters.add(auditLoggerFilter);
        filters.add(jwtFilter);
        return filters;
    }
}