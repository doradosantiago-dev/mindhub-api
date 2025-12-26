package com.mindhub.api.service.chatbot;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.mindhub.api.model.chatbot.ChatBotMessage;

/**
 * Servicio encargado de generar respuestas simuladas del chatbot.
 *
 * Procesa los mensajes recibidos, analiza el historial de conversación
 * y devuelve respuestas contextuales o predefinidas según el caso.
 *
 * Incluye lógica para reconocer saludos, preguntas frecuentes sobre
 * la plataforma y ofrecer ayuda sobre publicaciones, comentarios,
 * reacciones, perfil, reportes y notificaciones.
 */

@Service
public class ChatBotResponseService {

    private final Random random = new Random();

    public String generarRespuestaSimulada(String mensaje, List<ChatBotMessage> historial) {
        String mensajeLower = mensaje.toLowerCase().trim();

        String contextoAnterior = analizarContexto(historial);

        if (esUnSaludo(mensajeLower)) {
            return generarRespuestaSaludo(contextoAnterior);
        }

        if (mensajeLower.contains("qué tal") || mensajeLower.contains("como estas")) {
            return "¡Muy bien, gracias! Estoy aquí para ayudarte con cualquier duda sobre nuestra red social. ¿En qué puedo asistirte hoy?";
        }

        if (mensajeLower.contains("buenos días") || mensajeLower.contains("buenas tardes")
                || mensajeLower.contains("buenas noches")) {
            return "¡Saludos! Espero que estés teniendo un excelente día. ¿Necesitas ayuda con alguna función de la plataforma?";
        }

        if (mensajeLower.contains("como funciona") || mensajeLower.contains("qué puedo hacer")
                || mensajeLower.contains("funciones")) {
            return "Nuestra red social te permite:\n" +
                    "• Crear publicaciones públicas o privadas\n" +
                    "• Comentar en publicaciones de otros usuarios\n" +
                    "• Reaccionar con likes a las publicaciones\n" +
                    "• Buscar usuarios por nombre\n" +
                    "• Reportar contenido inapropiado\n" +
                    "• Configurar tu perfil como público o privado\n" +
                    "¿Qué te gustaría explorar primero?";
        }

        if (mensajeLower.contains("me puedes ayudar") || mensajeLower.contains("ayuda")) {
            return "¡Por supuesto! Estoy aquí para ayudarte. Puedo explicarte cómo:\n" +
                    "• Crear y gestionar publicaciones\n" +
                    "• Comentar y reaccionar\n" +
                    "• Buscar usuarios\n" +
                    "• Reportar contenido\n" +
                    "• Configurar tu perfil\n" +
                    "¿Qué te interesa más?";
        }

        if (mensajeLower.contains("crear publicación") || mensajeLower.contains("hacer publicación") ||
                mensajeLower.contains("publicar algo") || mensajeLower.contains("nueva publicación")) {
            return "Para crear una publicación:\n" +
                    "1. Ve al campo de texto principal\n" +
                    "2. Escribe tu contenido (máximo 500 caracteres)\n" +
                    "3. Selecciona privacidad: PÚBLICO (todos ven) o PRIVADO (solo tú)\n" +
                    "4. Haz clic en 'Publicar'\n" +
                    "¡Es así de fácil! ¿Qué quieres compartir?";
        }

        if (mensajeLower.contains("tipos de contenido") || mensajeLower.contains("qué puedo publicar")) {
            return "Puedes publicar:\n" +
                    "• Texto libre (máximo 500 caracteres)\n" +
                    "• Pensamientos y reflexiones\n" +
                    "• Actualizaciones personales\n" +
                    "• Noticias que quieras compartir\n" +
                    "• Cualquier contenido que quieras expresar\n" +
                    "Por ahora solo texto, pero ¡las posibilidades son infinitas!";
        }

        if (mensajeLower.contains("subir fotos") || mensajeLower.contains("imágenes")
                || mensajeLower.contains("fotos")) {
            return "Actualmente nuestra plataforma solo permite publicar texto. Las funcionalidades de fotos e imágenes están en desarrollo para futuras versiones. ¡Mientras tanto, puedes expresarte con palabras!";
        }

        if (mensajeLower.contains("editar publicación") || mensajeLower.contains("modificar publicación")) {
            return "Para editar una publicación:\n" +
                    "1. Ve a 'Mis Publicaciones'\n" +
                    "2. Encuentra la publicación que quieres editar\n" +
                    "3. Haz clic en el botón de editar\n" +
                    "4. Modifica el contenido\n" +
                    "5. Guarda los cambios\n" +
                    "Solo puedes editar tus propias publicaciones.";
        }

        if (mensajeLower.contains("eliminar publicación") || mensajeLower.contains("borrar publicación")) {
            return "Para eliminar una publicación:\n" +
                    "1. Ve a 'Mis Publicaciones'\n" +
                    "2. Encuentra la publicación que quieres eliminar\n" +
                    "3. Haz clic en el botón de eliminar\n" +
                    "4. Confirma la acción\n" +
                    "Esta acción no se puede deshacer.";
        }

        if (mensajeLower.contains("privacidad") || mensajeLower.contains("público privado")) {
            return "La privacidad de las publicaciones funciona así:\n" +
                    "• PÚBLICO: Todos los usuarios pueden ver, comentar y reaccionar\n" +
                    "• PRIVADO: Solo tú puedes ver la publicación\n" +
                    "Puedes cambiar la privacidad al crear la publicación. ¡Tú decides qué compartir!";
        }

        if (mensajeLower.contains("feed") || mensajeLower.contains("timeline") || mensajeLower.contains("inicio")) {
            return "El feed público muestra todas las publicaciones públicas de otros usuarios. No incluye tus propias publicaciones para que puedas ver contenido de la comunidad. Para ver tus publicaciones, ve a 'Mis Publicaciones'.";
        }

        if (mensajeLower.contains("ver publicaciones") || mensajeLower.contains("publicaciones de otros")) {
            return "Puedes ver publicaciones de otros usuarios de varias formas:\n" +
                    "• Feed público: Todas las publicaciones públicas\n" +
                    "• Perfil de usuario: Publicaciones públicas de un usuario específico\n" +
                    "• Solo se muestran publicaciones de usuarios con perfil público";
        }

        if (mensajeLower.contains("comentar") || mensajeLower.contains("hacer comentario")) {
            return "Para comentar en una publicación:\n" +
                    "1. Encuentra una publicación pública que te interese\n" +
                    "2. Busca el campo de comentarios debajo del post\n" +
                    "3. Escribe tu comentario (máximo 500 caracteres)\n" +
                    "4. Haz clic en 'Comentar'\n" +
                    "Solo puedes comentar en publicaciones de usuarios con perfil público.";
        }

        if (mensajeLower.contains("editar comentario") || mensajeLower.contains("modificar comentario")) {
            return "Para editar un comentario:\n" +
                    "1. Encuentra tu comentario\n" +
                    "2. Haz clic en el botón de editar\n" +
                    "3. Modifica el contenido\n" +
                    "4. Guarda los cambios\n" +
                    "Solo puedes editar tus propios comentarios.";
        }

        if (mensajeLower.contains("eliminar comentario") || mensajeLower.contains("borrar comentario")) {
            return "Para eliminar un comentario:\n" +
                    "1. Encuentra tu comentario\n" +
                    "2. Haz clic en el botón de eliminar\n" +
                    "3. Confirma la acción\n" +
                    "Solo puedes eliminar tus propios comentarios.";
        }

        if (mensajeLower.contains("ver comentarios") || mensajeLower.contains("comentarios de publicación")) {
            return "Para ver todos los comentarios de una publicación:\n" +
                    "1. Ve a cualquier publicación\n" +
                    "2. Busca la sección de comentarios\n" +
                    "3. Allí verás todos los comentarios ordenados por fecha\n" +
                    "Los comentarios son públicos y visibles para todos.";
        }

        if (mensajeLower.contains("reaccionar") || mensajeLower.contains("poner reacción")) {
            return "Para reaccionar a una publicación:\n" +
                    "1. Encuentra una publicación pública\n" +
                    "2. Busca los botones de reacciones debajo del post\n" +
                    "3. Haz clic en la emoción que quieres expresar\n" +
                    "4. Si ya reaccionaste, puedes cambiar tu reacción\n" +
                    "Solo puedes reaccionar en publicaciones de usuarios con perfil público.";
        }

        if (mensajeLower.contains("tipos de reacciones") || mensajeLower.contains("qué reacciones hay")) {
            return "Actualmente tenemos 1 tipo de reacción:\n" +
                    "• LIKE - Me gusta\n" +
                    "¡Expresa tu opinión con un like!";
        }

        if (mensajeLower.contains("cambiar reacción") || mensajeLower.contains("modificar reacción")) {
            return "Para cambiar tu reacción:\n" +
                    "1. Ve a la publicación donde ya reaccionaste\n" +
                    "2. Haz clic en una reacción diferente\n" +
                    "3. Tu reacción anterior se actualizará automáticamente\n" +
                    "No hay límite para cambiar reacciones.";
        }

        if (mensajeLower.contains("ver reacciones") || mensajeLower.contains("reacciones de publicación")) {
            return "Para ver todas las reacciones de una publicación:\n" +
                    "1. Ve a cualquier publicación\n" +
                    "2. Busca la sección de reacciones\n" +
                    "3. Allí verás el contador de cada tipo de reacción\n" +
                    "También puedes ver tu propia reacción en 'Mi Reacción'.";
        }

        // ===== PERFIL Y USUARIOS =====
        if (mensajeLower.contains("perfil") || mensajeLower.contains("mi perfil")) {
            return "Tu perfil incluye:\n" +
                    "• Información personal (nombre, apellidos, email)\n" +
                    "• Datos de contacto (teléfono, dirección)\n" +
                    "• Biografía personal\n" +
                    "• Foto de perfil\n" +
                    "• Configuración de privacidad (público/privado)\n" +
                    "Puedes editar tu perfil en cualquier momento.";
        }

        if (mensajeLower.contains("perfil público") || mensajeLower.contains("perfil privado")) {
            return "La configuración de perfil:\n" +
                    "• PÚBLICO: Otros usuarios pueden ver tu perfil y publicaciones\n" +
                    "• PRIVADO: Solo tú puedes ver tu información\n" +
                    "Los usuarios con perfil privado no pueden recibir comentarios ni reacciones.";
        }

        if (mensajeLower.contains("editar perfil") || mensajeLower.contains("modificar perfil")) {
            return "Para editar tu perfil:\n" +
                    "1. Ve a 'Mi Perfil'\n" +
                    "2. Haz clic en 'Editar'\n" +
                    "3. Modifica los campos que quieras\n" +
                    "4. Guarda los cambios\n" +
                    "Puedes actualizar tu información en cualquier momento.";
        }

        if (mensajeLower.contains("buscar usuarios") || mensajeLower.contains("encontrar usuarios")) {
            return "Para buscar usuarios:\n" +
                    "1. Usa la barra de búsqueda\n" +
                    "2. Escribe nombre o apellidos\n" +
                    "3. La búsqueda es automática y no distingue mayúsculas/minúsculas\n" +
                    "4. Solo aparecen usuarios con perfil público y activos\n" +
                    "¡Encuentra a tus amigos fácilmente!";
        }

        if (mensajeLower.contains("seguir") || mensajeLower.contains("seguidor") || mensajeLower.contains("follow")) {
            return "¡Ahora tenemos sistema de seguidores!\n" +
                    "• Puedes seguir a usuarios con perfil público\n" +
                    "• Si sigues a un usuario privado, podrás ver su contenido\n" +
                    "• Ve a 'Gestionar seguidores' para ver a quién sigues\n" +
                    "• También puedes ver quién te sigue\n" +
                    "• El dashboard muestra tu contador de seguidores\n" +
                    "¡Construye tu red de conexiones!";
        }

        if (mensajeLower.contains("dejar de seguir") || mensajeLower.contains("unfollow")) {
            return "Para dejar de seguir a alguien:\n" +
                    "1. Ve a 'Gestionar seguidores'\n" +
                    "2. Busca la pestaña 'Siguiendo'\n" +
                    "3. Encuentra al usuario\n" +
                    "4. Haz clic en 'Dejar de seguir'\n" +
                    "El usuario no será notificado de esta acción.";
        }

        if (mensajeLower.contains("quién me sigue") || mensajeLower.contains("ver seguidores")) {
            return "Para ver tus seguidores:\n" +
                    "1. Ve a 'Gestionar seguidores'\n" +
                    "2. En la pestaña 'Seguidores' verás la lista completa\n" +
                    "3. Puedes ver su perfil haciendo clic en su nombre\n" +
                    "También puedes ver el contador en tu dashboard.";
        }

        if (mensajeLower.contains("reportar") || mensajeLower.contains("denunciar")) {
            return "Para reportar contenido inapropiado:\n" +
                    "1. Ve a la publicación que quieres reportar\n" +
                    "2. Busca el botón de reportar\n" +
                    "3. Selecciona el motivo del reporte\n" +
                    "4. Añade una descripción detallada\n" +
                    "5. Envía el reporte\n" +
                    "Los administradores revisarán tu reporte.";
        }

        if (mensajeLower.contains("motivos reporte") || mensajeLower.contains("por qué reportar")) {
            return "Puedes reportar contenido por:\n" +
                    "• Contenido inapropiado\n" +
                    "• Acoso o bullying\n" +
                    "• Spam o contenido comercial no autorizado\n" +
                    "• Información falsa o engañosa\n" +
                    "• Violación de derechos de autor\n" +
                    "• Cualquier contenido que viole nuestras normas";
        }

        if (mensajeLower.contains("qué pasa con reportes") || mensajeLower.contains("revisión reportes")) {
            return "Cuando reportas contenido:\n" +
                    "1. El reporte se envía a los administradores\n" +
                    "2. Los admins revisan el contenido reportado\n" +
                    "3. Pueden tomar acciones como eliminar contenido o advertir usuarios\n" +
                    "4. Se te notificará el resultado del reporte\n" +
                    "Tu privacidad está protegida.";
        }

        if (mensajeLower.contains("notificaciones") || mensajeLower.contains("alertas")) {
            return "Recibirás notificaciones cuando:\n" +
                    "• Alguien comente en tu publicación\n" +
                    "• Alguien reaccione a tu publicación\n" +
                    "• Se resuelva un reporte que hiciste\n" +
                    "• Los administradores tomen acciones sobre tu contenido\n" +
                    "Las notificaciones aparecen en tiempo real.";
        }

        if (mensajeLower.contains("ver notificaciones") || mensajeLower.contains("mis notificaciones")) {
            return "Para ver tus notificaciones:\n" +
                    "1. Busca el ícono de notificaciones\n" +
                    "2. Haz clic para ver todas tus alertas\n" +
                    "3. Las notificaciones están ordenadas por fecha\n" +
                    "4. Puedes marcar como leídas las que ya revisaste";
        }

        if (mensajeLower.contains("administrador") || mensajeLower.contains("admin")) {
            return "Los administradores tienen funciones especiales:\n" +
                    "• Revisar reportes de contenido\n" +
                    "• Eliminar publicaciones inapropiadas\n" +
                    "• Advertir o suspender usuarios\n" +
                    "• Gestionar la moderación de la plataforma\n" +
                    "Si necesitas contactar a un admin, usa el sistema de reportes.";
        }

        if (mensajeLower.contains("moderación") || mensajeLower.contains("moderar")) {
            return "La moderación en nuestra plataforma:\n" +
                    "• Es realizada por administradores\n" +
                    "• Se basa en reportes de usuarios\n" +
                    "• Sigue políticas claras de contenido\n" +
                    "• Mantiene un ambiente seguro para todos\n" +
                    "Todos los usuarios pueden contribuir reportando contenido inapropiado.";
        }

        if (mensajeLower.contains("error") || mensajeLower.contains("problema") ||
                mensajeLower.contains("no funciona") || mensajeLower.contains("bug")) {
            return "Si tienes problemas técnicos:\n" +
                    "1. Intenta recargar la página\n" +
                    "2. Verifica tu conexión a internet\n" +
                    "3. Limpia el caché del navegador\n" +
                    "4. Si el problema persiste, contacta soporte técnico\n" +
                    "¿Puedes describir específicamente qué error estás viendo?";
        }

        if (mensajeLower.contains("no puedo") || mensajeLower.contains("no me deja")) {
            return "Si no puedes realizar una acción:\n" +
                    "• Verifica que estés logueado\n" +
                    "• Asegúrate de tener permisos para esa función\n" +
                    "• Revisa que el contenido no haya sido eliminado\n" +
                    "• Contacta soporte si el problema persiste\n" +
                    "¿Qué acción específica no puedes realizar?";
        }

        if (mensajeLower.contains("seguridad") || mensajeLower.contains("proteger cuenta")) {
            return "Para proteger tu cuenta:\n" +
                    "• Usa una contraseña fuerte y única\n" +
                    "• No compartas tus credenciales\n" +
                    "• Cierra sesión en dispositivos públicos\n" +
                    "• Reporta cualquier actividad sospechosa\n" +
                    "• Configura tu perfil según tu preferencia de privacidad";
        }

        if (mensajeLower.contains("contraseña") || mensajeLower.contains("password")) {
            return "Para gestionar tu contraseña:\n" +
                    "• Ve a configuración de cuenta\n" +
                    "• Selecciona 'Cambiar contraseña'\n" +
                    "• Introduce tu contraseña actual\n" +
                    "• Crea una nueva contraseña segura\n" +
                    "• Confirma el cambio\n" +
                    "Usa una contraseña que no uses en otros sitios.";
        }

        // ===== DASHBOARD Y ESTADÍSTICAS =====
        if (mensajeLower.contains("dashboard") || mensajeLower.contains("estadísticas")
                || mensajeLower.contains("panel")) {
            return "En tu Dashboard verás:\n" +
                    "• Estadísticas: Posts creados, seguidores y likes recibidos\n" +
                    "• Actividades recientes: Notificaciones sobre comentarios y reacciones\n" +
                    "• Vista de tu perfil\n" +
                    "• Acceso rápido al feed\n" +
                    "¡Es tu centro de control personal!";
        }

        if (mensajeLower.contains("actividades recientes") || mensajeLower.contains("actividad")) {
            return "Las actividades recientes muestran:\n" +
                    "• Comentarios en tus publicaciones\n" +
                    "• Reacciones/likes a tu contenido\n" +
                    "• Respuestas a reportes que hiciste\n" +
                    "• Acciones administrativas en tu cuenta\n" +
                    "Se actualizan en tiempo real y están paginadas.";
        }

        if (mensajeLower.contains("chatbot") || mensajeLower.contains("asistente virtual")
                || mensajeLower.contains("bot")) {
            return "¡Soy tu chatbot asistente! Puedo ayudarte con:\n" +
                    "• Explicar todas las funciones de la app\n" +
                    "• Resolver dudas sobre publicaciones, comentarios y reacciones\n" +
                    "• Guiarte en la configuración de perfil y privacidad\n" +
                    "• Información sobre seguidores y búsquedas\n" +
                    "• Procedimientos de reportes y moderación\n" +
                    "¡Pregunta lo que necesites!";
        }

        if (mensajeLower.contains("feed personal") || mensajeLower.contains("mi feed")) {
            return "Tu feed personal muestra:\n" +
                    "• Posts públicos de usuarios que sigues\n" +
                    "• Todos tus posts (públicos y privados)\n" +
                    "• Posts de usuarios privados si los sigues\n" +
                    "Es diferente del feed público que solo muestra posts de todos los usuarios públicos.";
        }

        if (mensajeLower.contains("diferencia feed") || mensajeLower.contains("feeds")) {
            return "Tenemos dos tipos de feed:\n\n" +
                    "**Feed Público:**\n" +
                    "• Posts de todos los usuarios con perfil público\n" +
                    "• No incluye tus propios posts\n" +
                    "• Para descubrir contenido de la comunidad\n\n" +
                    "**Feed Personal (Dashboard):**\n" +
                    "• Tus posts + posts de quien sigues\n" +
                    "• Incluye posts privados de usuarios que sigues\n" +
                    "• Contenido personalizado para ti";
        }

        if (mensajeLower.contains("eliminar cuenta") || mensajeLower.contains("borrar cuenta")) {
            return "Para eliminar tu cuenta:\n" +
                    "1. Ve a 'Configuración'\n" +
                    "2. Busca la opción 'Eliminar cuenta'\n" +
                    "3. Lee las advertencias cuidadosamente\n" +
                    "4. Confirma la eliminación\n" +
                    "⚠️ Esta acción es IRREVERSIBLE:\n" +
                    "• Se eliminan todas tus publicaciones\n" +
                    "• Se pierden todos tus comentarios\n" +
                    "• Se borran tus reacciones\n" +
                    "• No podrás recuperar tu cuenta";
        }

        if (mensajeLower.contains("usuario privado") || mensajeLower.contains("post privado")) {
            return "Entendiendo privacidad:\n\n" +
                    "**Usuario Privado:**\n" +
                    "• Solo quienes te siguen ven tu contenido\n" +
                    "• No apareces en búsquedas\n" +
                    "• Más control sobre tu audiencia\n\n" +
                    "**Post Privado:**\n" +
                    "• Solo tú puedes verlo\n" +
                    "• Útil para borradores o notas personales\n" +
                    "• Nadie puede comentar o reaccionar\n\n" +
                    "Puedes combinar ambos niveles de privacidad.";
        }

        if (mensajeLower.contains("límite") || mensajeLower.contains("máximo caracteres")
                || mensajeLower.contains("longitud")) {
            return "Límites de caracteres:\n" +
                    "• Publicaciones: 500 caracteres máximo\n" +
                    "• Comentarios: 500 caracteres máximo\n" +
                    "• Biografía de perfil: 500 caracteres\n" +
                    "• Razón de reporte: Sin límite específico\n" +
                    "¡Expresa tus ideas de forma concisa!";
        }

        if (mensajeLower.contains("buscar posts") || mensajeLower.contains("buscar publicaciones")) {
            return "Actualmente no hay búsqueda directa de posts, pero puedes:\n" +
                    "• Ver el feed público con todos los posts\n" +
                    "• Filtrar por usuario usando la búsqueda de usuarios\n" +
                    "• Ver posts de un usuario en su perfil\n" +
                    "• Usar tu feed personal para contenido de quien sigues\n" +
                    "¡La búsqueda de contenido está en desarrollo!";
        }

        if (mensajeLower.contains("notificación reporte") || mensajeLower.contains("respuesta reporte")) {
            return "Cuando haces un reporte:\n" +
                    "1. Los admins lo revisan\n" +
                    "2. Recibes notificación del resultado\n" +
                    "3. Si fue ACEPTADO: 'Tu reporte ha sido resuelto. La publicación ha sido eliminada.'\n" +
                    "4. Si fue RECHAZADO: 'Tu reporte ha sido revisado y rechazado.'\n" +
                    "Las notificaciones aparecen en tu dashboard y en el centro de notificaciones.";
        }

        if (mensajeLower.contains("mis publicaciones") || mensajeLower.contains("ver mis posts")) {
            return "Para ver tus publicaciones:\n" +
                    "1. Ve al menú lateral\n" +
                    "2. Haz clic en 'Mis Publicaciones'\n" +
                    "3. Verás todos tus posts (públicos y privados)\n" +
                    "4. Puedes editarlos o eliminarlos\n" +
                    "También aparecen en tu feed personal del dashboard.";
        }

        if (mensajeLower.contains("privacidad") || mensajeLower.contains("datos personales")) {
            return "Tu privacidad es importante:\n" +
                    "• Solo compartimos información que tú autorizas\n" +
                    "• Puedes configurar tu perfil como privado\n" +
                    "• Los administradores respetan tu privacidad\n" +
                    "• Puedes eliminar tu cuenta en cualquier momento\n" +
                    "• Tus datos están protegidos según las leyes vigentes";
        }

        if (mensajeLower.contains("gracias")) {
            return "¡De nada! Me alegra haber podido ayudarte. Si tienes más preguntas sobre la plataforma, no dudes en preguntarme. ¡Estoy aquí para asistirte!";
        }

        if (mensajeLower.contains("perfecto") || mensajeLower.contains("excelente")
                || mensajeLower.contains("genial")) {
            return "¡Me alegra que te haya servido la información! Si necesitas ayuda con cualquier otra función de la plataforma, aquí estaré. ¡Que tengas un excelente día!";
        }

        return generarRespuestaContextual(mensaje, contextoAnterior);
    }

    private boolean esUnSaludo(String mensaje) {
        return mensaje.contains("hola") || mensaje.contains("buenas") || mensaje.contains("saludos") ||
                mensaje.contains("hey");
    }

    private String analizarContexto(List<ChatBotMessage> historial) {
        if (historial.isEmpty())
            return "first_time";

        int mensajesUsuario = 0;
        String ultimoTema = "";

        for (int i = Math.max(0, historial.size() - 5); i < historial.size(); i++) {
            ChatBotMessage msg = historial.get(i);
            if (msg.getType() == com.mindhub.api.model.enums.MessageType.USER) {
                mensajesUsuario++;
                String contenido = msg.getContent().toLowerCase();
                if (contenido.contains("publicar"))
                    ultimoTema = "posts";
                else if (contenido.contains("comentar"))
                    ultimoTema = "comments";
                else if (contenido.contains("reaccionar"))
                    ultimoTema = "reactions";
                else if (contenido.contains("perfil"))
                    ultimoTema = "profile";
            }
        }

        if (mensajesUsuario > 3)
            return "long_conversation";
        if (!ultimoTema.isEmpty())
            return ultimoTema;
        return "new_conversation";
    }

    private String generarRespuestaSaludo(String contexto) {
        String[] saludos = {
                "¡Hola! Soy tu asistente virtual de la red social. ¿En qué puedo ayudarte hoy?",
                "¡Bienvenido! Estoy aquí para ayudarte con cualquier duda sobre la aplicación.",
                "¡Hola! Me alegra verte por aquí. ¿Tienes alguna pregunta sobre cómo usar la plataforma?",
                "¡Saludos! Soy tu guía personal para navegar por todas las funciones de la red social."
        };

        String saludoBase = saludos[random.nextInt(saludos.length)];

        if (contexto.equals("first_time")) {
            saludoBase += " Como veo que es tu primera vez, puedo explicarte las funciones principales: publicaciones, comentarios, reacciones y más.";
        }

        return saludoBase;
    }

    private String generarRespuestaContextual(String mensaje, String contexto) {
        if (contexto.equals("posts")) {
            return "Veo que estás interesado en las publicaciones. ¿Te gustaría saber cómo crear una, editar contenido, o configurar la privacidad?";
        } else if (contexto.equals("comments")) {
            return "Sobre comentarios, ¿necesitas ayuda para comentar, editar o ver comentarios de otros usuarios?";
        } else if (contexto.equals("reactions")) {
            return "Respecto a las reacciones, ¿quieres conocer los tipos disponibles o cómo reaccionar a publicaciones?";
        } else if (contexto.equals("profile")) {
            return "Sobre tu perfil, ¿necesitas ayuda para configurarlo, editarlo o entender la privacidad?";
        } else {
            return "No estoy seguro de entender tu pregunta. ¿Podrías reformularla? Puedo ayudarte con publicaciones, comentarios, reacciones, perfil, búsqueda, reportes y más. ¿Qué te interesa?";
        }
    }
}
