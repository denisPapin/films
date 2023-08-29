package com.dp.homework.films.constants;

import java.util.List;

public interface SecurityConstants {
    class REST {
        public static List<String> FILMS_WHITE_LIST = List.of("/rest/films",
                "/rest/films/search",
                "/rest/films/{id}",
                "/rest/films/getAll");

        public static List<String> DIRECTORS_WHITE_LIST = List.of("/rest/directors",
                "/rest/directors/search",
                "/rest/directors/getAll",
                "/rest/films/search/director",
                "/rest/directors/{id}");

        public static List<String> USERS_WHITE_LIST = List.of("/rest/users/auth",
                "/rest/users/registration",
                "/rest/users/remember-password",
                "/rest/users/change-password",
                "/rest/users/getAll",
                "/rest/users/soft-delete",
                "/rest/users/soft-delete/{id}",
                "/rest/users/restore",
                "/rest/users/restore/{id}",
                "/rest/users/update",
                "/rest/users/add",
                "/rest/users/{id}"
        );

        public static List<String> DIRECTORS_PERMISSION_LIST = List.of(
                "/rest/directors/add",
                "/rest/directors/update",
                "/rest/directors/soft-delete",
                "/rest/directors/soft-delete/{id}",
                "/rest/directors/restore/{id}",
                "/rest/directors/addFilm/{id}",
                "/rest/directors/addFilm"
        );

        public static List<String> FILMS_PERMISSION_LIST = List.of("/rest/films/add",
                "/rest/films/update",
                "/rest/films/delete/**",
                "/rest/films/delete/{id}",
                "/rest/films/download/{filmId}",
                "/rest/films/soft-delete",
                "/rest/films/soft-delete/{id}",
                "/rest/films/restore",
                "/rest/films/restore/{id}",
                "rest/films/addDirector",
                "rest/films/addDirector/{id}"




                );
        public static List<String> ORDERS_PERMISSION_LIST = List.of(
                "/rest/orders/update",
                "/rest/orders/delete/**",
                "/rest/orders/delete/{id}",
                "/rest/orders/download/{filmId}",
                "/rest/orders/soft-delete",
                "/rest/orders/soft-delete/{id}",
                "/rest/orders/restore",
                "/rest/orders/restore/{id}",
                "/rest/orders/getAll",
                "/rest/orders/{id}",
                "/rest/orders/get-order/{userId}",
                "/rest/orders/create-order"
        );


        public static List<String> USERS_PERMISSION_LIST = List.of("/rest/rent/film/*");
    }

    List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/js/**",
            "/css/**",
            "/",
            // -- Swagger UI v3 (OpenAPI)
            "/swagger-ui/**",
            "/webjars/bootstrap/5.0.2/**",
            "/v3/api-docs/**",
            "/error", "/users",
            "/rest/users/auth");

    List<String> FILMS_WHITE_LIST = List.of("/films",
            "/films/search",
            "/films/{id}");

    List<String> DIRECTORS_WHITE_LIST = List.of("/directors",
            "/directors/search",
            "/films/search/director",
            "/directors/{id}");
    List<String> FILMS_PERMISSION_LIST = List.of("/films/add",
            "/films/add/{id}",
            "/films/update",
            "/films/update/{id}",
            "/films/delete",
            "/films/delete/{filmId}",
            "/films/restore/{id}",
            "/films/restore",
            "/films/download/{filmId}",
            "/films/add-director/{filmId}",
            "/films/add-director",
            "/films/search/director"
            );

    List<String> DIRECTORS_PERMISSION_LIST = List.of("/directors/add",
            "/directors/update",
            "/directors/search",
            "/directors/update/{id}",
            "/directors/soft-delete",
            "/directors/soft-delete/{id}",
            "/directors/delete/{id}",
            "/directors/add-film/{id}",
            "/directors/add-film",
            "/directors/restore/{id}");

    List<String> USERS_WHITE_LIST = List.of("/login",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password",
            "/rent/get-film/*",
            "/rent/get-film",
            "/users/profile/*",
            "/users/profile",
            "/rent/user-films",
            "/rent/user-films/*",
            "/rent/return-film/*",
            "/rent/return-film",
            "/users/profile/{id}",
            "/users/profile/update/{id}"
            );

    List<String> USERS_PERMISSION_LIST = List.of("/rent/film/*", "/rent/get-film/*",
            "/users/add-manager",
            "/users/add-manager/{id}",
            "/users/profile/update/{id}",
            "/users/update"
    );
}

