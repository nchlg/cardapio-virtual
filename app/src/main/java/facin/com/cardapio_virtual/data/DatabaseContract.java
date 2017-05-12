package facin.com.cardapio_virtual.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by 13108306 on 03/01/2017.
 */
public class DatabaseContract {
    /* "Content authority" é o nome para o provedor de conteúdo, similiar a relação entre
    um domínio e um site. Uma string conveniente para se usar é o pacote da aplicação por
    ser um valor único. */
    public static final String CONTENT_AUTHORITY = "facin.com.cardapio_virtual";

    /* Use a "Content authority" para criar a base de todas URIs que o aplicativo usará para
    * se comunicar com o provedor de conteúdo. */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RESTAURANTES = "restaurantes";
    public static final String PATH_LOGS = "logs";
    public static final String PATH_MAES_FILHAS = "maes_filhas";
    public static final String PATH_JOIN_MAESFILHAS_LOGS = "join_maesfilhas_logs";
    public static final String PATH_USUARIOS = "usuarios";
    public static final String PATH_FAVORITOS = "favoritos";

    /* Joins */
    /* Restaurantes-Favoritos */
    public static final String PATH_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS = "restaurantes-favoritos";
    public static final Uri CONTENT_URI_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS).build();
    public static final String CONTENT_ITEM_TYPE_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS;

    /* Classe interna que define os conteúdos da tabela Restuarantes */
    public static final class RestaurantesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANTES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANTES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANTES;

        /* Nome da tabela */
        public static final String TABLE_NAME = "restaurantes";

        /* Colunas */
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_ENDERECO = "endereco";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LNG = "longitude";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_FAVORITO = "favorito";

        /* Gera o ID da tabela */
        public static Uri buildAvaliacoesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Log */
    public static final class LogsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOGS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOGS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOGS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "logs";

        /* Colunas */
        public static final String COLUMN_PRODUTO = "produto";
        public static final String COLUMN_ACESSOS = "acessos";

        /* Gera o ID da tabela */
        public static Uri buildAvaliacoesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Maes_Filhas */
    public static final class MaesFilhasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MAES_FILHAS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAES_FILHAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAES_FILHAS;

        public static final Uri CONTENT_URI_JOIN = BASE_CONTENT_URI.buildUpon().appendPath(PATH_JOIN_MAESFILHAS_LOGS).build();

        /* Nome da tabela */
        public static final String TABLE_NAME = "maes_filhas";

        /* Colunas */
        public static final String COLUMN_MAE = "nome_mae";
        public static final String COLUMN_FILHA = "id_filha";

        /* Gera o ID da tabela */
        public static Uri buildAvaliacoesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Usuarios */
//    public static final class UsuariosEntry implements BaseColumns {
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USUARIOS).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIOS;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIOS;
//
//        /* Nome da tabela */
//        public static final String TABLE_NAME = "usuarios";
//
//        /* Colunas */
//        public static final String COLUMN_NOME = "nome";
//        // public static final String COLUMN_EMAIL = "email";
//
//        /* Gera o ID da tabela */
//        public static Uri buildAvaliacoesUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }

//
//    /* Classe interna que define os conteúdos da tabela Favoritos */
//    public static final class FavoritosEntry implements BaseColumns {
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITOS).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITOS;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITOS;
//
//        /* Nome da tabela */
//        public static final String TABLE_NAME = "favoritos";
//
//        /* Colunas */
//        public static final String COLUMN_ID_RESTAURANTE = "id_restaurante";
//        public static final String COLUMN_ID_USUARIO = "id_usuario";
//
//        /* Gera o ID da tabela */
//        public static Uri buildAvaliacoesUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }

}
