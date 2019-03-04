package business.jwt;

import models.Admin;
import play.libs.typedmap.TypedKey;

public class JwtAttrs {
    public static final TypedKey<Admin> VERIFIED_ADMIN = TypedKey.<Admin>create("admin");
}
