-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA - MINDHUB
-- Este archivo contiene INSERTs de ejemplo para ingestar
-- datos de prueba en la base de datos MindHub.

-- =====================================================
-- INSERTAR USUARIOS DE PRUEBA
-- =====================================================

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('maria_garcia', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Maria', 'Garcia', 'maria.garcia@email.com', '+34 600 111 222', 'Calle Gran Via 45, Madrid', 'Desarrolladora Frontend apasionada por Angular y diseno UX. Amante de la tecnologia y la innovacion.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '30 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('carlos_rodriguez', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Carlos', 'Rodriguez', 'carlos.rodriguez@email.com', '+34 600 333 444', 'Avenida Diagonal 123, Barcelona', 'Desarrollador Backend especializado en Spring Boot y Java. Me encanta crear APIs robustas y escalables.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '1 day' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('ana_lopez', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Ana', 'Lopez', 'ana.lopez@email.com', '+34 600 555 666', 'Calle Valencia 78, Valencia', 'UX/UI Designer con experiencia en diseno de interfaces web y moviles. Creo experiencias digitales memorables.', 'PRIVATE', 2, true, CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('david_martinez', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'David', 'Martinez', 'david.martinez@email.com', '+34 600 777 888', 'Plaza Mayor 15, Sevilla', 'DevOps Engineer especializado en Docker, Kubernetes y CI/CD. Me apasiona la automatizacion y la infraestructura como codigo.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '15 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('laura_sanchez', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Laura', 'Sanchez', 'laura.sanchez@email.com', '+34 600 999 000', 'Calle Real 234, Malaga', 'Data Scientist con experiencia en machine learning y analisis de datos. Transformo datos en insights valiosos.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '10 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '1 day' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('javier_fernandez', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Javier', 'Fernandez', 'javier.fernandez@email.com', '+34 600 111 333', 'Avenida de la Constitucion 67, Granada', 'Full Stack Developer con experiencia en React, Node.js y bases de datos. Me encanta aprender nuevas tecnologias.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '8 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, first_name, last_name, email, phone, address, biography, privacy_type, role_id, active, registration_date, last_activity_date) VALUES
('carmen_ruiz', '$2a$10$GrgPf1ajF9sYOy3S91JSROKA38HNpmup.DDfK2KWRlHVmzyHddHj.', 'Carmen', 'Ruiz', 'carmen.ruiz@email.com', '+34 600 444 555', 'Calle San Miguel 89, Bilbao', 'Product Manager con experiencia en metodologias agiles. Me apasiona crear productos que resuelvan problemas reales.', 'PUBLIC', 2, true, CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '1 day' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval)
ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- INSERTAR PERFILES DE USUARIO DE PRUEBA
-- =====================================================

INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1992-03-15', 'Frontend Developer', 'Angular, React, UX/UI, Diseno Web, Tecnologia', 'https://maria-garcia.dev', 'Madrid, Espana', 'linkedin.com/in/mariagarcia', 'Ingenieria Informatica - Universidad Complutense de Madrid', 'TechCorp Solutions', CURRENT_TIMESTAMP - INTERVAL '30 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'maria_garcia'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1988-07-22', 'Backend Developer', 'Java, Spring Boot, Microservicios, APIs, Base de Datos', 'https://carlos-rodriguez.dev', 'Barcelona, Espana', 'linkedin.com/in/carlosrodriguez', 'Ingenieria de Software - Universidad Politecnica de Cataluna', 'Innovation Labs', CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'carlos_rodriguez'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1990-11-08', 'UX/UI Designer', 'Diseno de Interfaces, User Research, Prototipado, Figma', 'https://ana-lopez.design', 'Valencia, Espana', 'linkedin.com/in/analopez', 'Diseno Grafico - Universidad de Valencia', 'Creative Studio', CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'ana_lopez'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1985-04-12', 'DevOps Engineer', 'Docker, Kubernetes, AWS, CI/CD, Automatizacion', 'https://david-martinez.dev', 'Sevilla, Espana', 'linkedin.com/in/davidmartinez', 'Ingenieria de Sistemas - Universidad de Sevilla', 'Cloud Solutions', CURRENT_TIMESTAMP - INTERVAL '15 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'david_martinez'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1993-09-30', 'Data Scientist', 'Machine Learning, Python, Analisis de Datos, IA', 'https://laura-sanchez.dev', 'Malaga, Espana', 'linkedin.com/in/laurasanchez', 'Matematicas Aplicadas - Universidad de Malaga', 'Data Insights Corp', CURRENT_TIMESTAMP - INTERVAL '10 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'laura_sanchez'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1991-12-05', 'Full Stack Developer', 'React, Node.js, MongoDB, JavaScript, TypeScript', 'https://javier-fernandez.dev', 'Granada, Espana', 'linkedin.com/in/javierfernandez', 'Ingenieria Informatica - Universidad de Granada', 'Digital Solutions', CURRENT_TIMESTAMP - INTERVAL '8 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'javier_fernandez'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO user_profiles (birth_date, occupation, interests, website, location, social_media, education, workplace, creation_date, user_id)
SELECT '1987-06-18', 'Product Manager', 'Agile, Scrum, Product Strategy, User Stories', 'https://carmen-ruiz.dev', 'Bilbao, Espana', 'linkedin.com/in/carmenruiz', 'Administracion de Empresas - Universidad del Pais Vasco', 'Product Hub', CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'carmen_ruiz'
ON CONFLICT (user_id) DO NOTHING;


-- =====================================================
-- INSERTAR PUBLICACIONES DE PRUEBA
-- =====================================================

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Hola comunidad! Acabo de terminar un proyecto increible con Angular 20 y Material Design. La nueva sintaxis de control flow (@if, @for) es simplemente genial. Que opinais de las nuevas caracteristicas? #Angular #Angular20 #Frontend #MaterialDesign', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '28 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '28 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'maria_garcia' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'maria_garcia') AND content LIKE '%Angular 20%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Compartiendo algunos tips de UX que he aprendido: Siempre proporciona feedback visual inmediato, Manten la consistencia en el diseno, Prioriza la accesibilidad, Simplifica los flujos de usuario. Que otros principios de UX considerais fundamentales? #UX #Design #UserExperience', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'maria_garcia' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'maria_garcia') AND content LIKE '%tips de UX%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Hoy he estado optimizando consultas SQL en PostgreSQL. Algunas lecciones aprendidas: Los indices son cruciales para el rendimiento, EXPLAIN ANALYZE es tu mejor amigo, Evita N+1 queries a toda costa. Teneis alguna consulta que os este dando problemas? #PostgreSQL #SQL #Backend #Performance', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'carlos_rodriguez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'carlos_rodriguez') AND content LIKE '%PostgreSQL%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Implementando autenticacion JWT en Spring Boot 3.5.5. La nueva configuracion de seguridad es mucho mas limpia y flexible. Alguien mas esta migrando a Spring Security 6? #SpringBoot #JWT #Security #Java', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'carlos_rodriguez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'carlos_rodriguez') AND content LIKE '%JWT%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Disenando una nueva interfaz para una aplicacion de gestion de tareas. El desafio: crear algo intuitivo y hermoso al mismo tiempo. Aqui esta mi proceso: 1. User Research, 2. Wireframes, 3. Prototipado, 4. Testing. Que metodologia de diseno preferis? #UXDesign #UI #DesignProcess', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'ana_lopez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'ana_lopez') AND content LIKE '%UXDesign%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Configurando un pipeline de CI/CD con GitHub Actions y Docker. Automatizar el despliegue ha sido un game-changer para nuestro equipo. Build automatico en cada push, Contenedores Docker para consistencia, Despliegue automatico a staging. Que herramientas de CI/CD usais? #DevOps #Docker #GitHubActions #CI/CD', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '14 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '14 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'david_martinez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'david_martinez') AND content LIKE '%GitHub Actions%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Analizando datos de rendimiento de nuestra aplicacion. Los insights que hemos obtenido son fascinantes: 40% de mejora en tiempo de carga, Reduccion del 60% en errores, 25% mas de engagement en movil. Que metricas considerais mas importantes? #DataScience #Analytics #Performance #Insights', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '9 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '9 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'laura_sanchez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'laura_sanchez') AND content LIKE '%DataScience%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Construyendo una aplicacion full-stack con React + Node.js + MongoDB. La stack MERN sigue siendo increiblemente potente para prototipado rapido. React para el frontend, Node.js para el backend, MongoDB para la base de datos. Que stack preferis para desarrollo full-stack? #MERN #FullStack #React #NodeJS', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'javier_fernandez' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'javier_fernandez') AND content LIKE '%MERN%');

INSERT INTO posts (content, image_url, privacy_type, creation_date, update_date, author_id)
SELECT 'Como Product Manager, una de las cosas mas importantes que he aprendido es la importancia de la comunicacion efectiva entre equipos tecnicos y stakeholders. Escuchar activamente, Documentar decisiones, Facilitar colaboracion. Que estrategias usais para mejorar la comunicacion en equipos? #ProductManagement #Agile #Communication', NULL, 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, id
FROM users WHERE username = 'carmen_ruiz' AND NOT EXISTS (SELECT 1 FROM posts WHERE author_id = (SELECT id FROM users WHERE username = 'carmen_ruiz') AND content LIKE '%ProductManagement%');

-- =====================================================
-- INSERTAR COMENTARIOS DE PRUEBA
-- =====================================================

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Excelente iniciativa! Estoy emocionado de ser parte de esta comunidad.', CURRENT_TIMESTAMP - INTERVAL '24 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '24 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'maria_garcia' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Excelente iniciativa%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Me encanta la idea de conectar con otros desarrolladores. Cuenta conmigo!', CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Me encanta la idea%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Bienvenidos todos! Esta plataforma va a ser increible.', CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'ana_lopez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Bienvenidos todos%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Muy interesante! Podrias compartir algun ejemplo de codigo de la implementacion?', CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'maria_garcia' AND p.content LIKE '%WebSockets%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Muy interesante%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'WebSockets son geniales para notificaciones en tiempo real. Usaste STOMP o implementacion nativa?', CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%WebSockets%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%WebSockets son geniales%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Has probado las nuevas signals? Son una revolucion para el estado de la aplicacion.', CURRENT_TIMESTAMP - INTERVAL '26 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '26 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%Angular 20%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%nuevas signals%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Tambien es importante considerar la accesibilidad desde el principio del diseno.', CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'ana_lopez' AND p.content LIKE '%tips de UX%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%accesibilidad%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Los indices son clave. Has probado los indices parciales para casos especificos?', CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'david_martinez' AND p.content LIKE '%PostgreSQL%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%indices son clave%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Como manejas la renovacion de tokens JWT?', CURRENT_TIMESTAMP - INTERVAL '16 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '16 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'maria_garcia' AND p.content LIKE '%Spring Boot 3.5.5%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%renovacion de tokens%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Me encanta Figma para prototipado. Has probado Framer para animaciones?', CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'maria_garcia' AND p.content LIKE '%UXDesign%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Figma%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Tambien uso Jenkins, pero GitHub Actions es mas facil de configurar.', CURRENT_TIMESTAMP - INTERVAL '12 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '12 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%GitHub Actions%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%Jenkins%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Python con pandas y matplotlib son mis favoritas para analisis.', CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'laura_sanchez' AND p.content LIKE '%DataScience%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%pandas%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'TypeScript hace el desarrollo mucho mas seguro. Lo uso en todo el stack.', CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'maria_garcia' AND p.content LIKE '%MERN%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%TypeScript%');

INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT 'Tambien es importante documentar las decisiones tecnicas para futuras referencias.', CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%ProductManagement%'
    AND NOT EXISTS (SELECT 1 FROM comments c WHERE c.author_id = u.id AND c.post_id = p.id AND c.content LIKE '%documentar%');

-- =====================================================
-- INSERTAR REACCIONES DE PRUEBA
-- =====================================================
INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '24 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'david_martinez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'laura_sanchez' AND p.content LIKE '%Bienvenidos a MindHub%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%WebSockets%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%WebSockets%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%WebSockets%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '26 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%Angular 20%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id)
    ON CONFLICT (user_id, post_id) DO NOTHING;

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%Angular 20%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '24 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'david_martinez' AND p.content LIKE '%Angular 20%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%tips de UX%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'david_martinez' AND p.content LIKE '%tips de UX%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'laura_sanchez' AND p.content LIKE '%tips de UX%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%PostgreSQL%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%PostgreSQL%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'david_martinez' AND p.content LIKE '%PostgreSQL%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '16 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%Spring Boot 3.5.5%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '15 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%Spring Boot 3.5.5%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%UXDesign%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '16 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%UXDesign%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '12 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%GitHub Actions%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '11 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%GitHub Actions%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '10 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%GitHub Actions%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%DataScience%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '6 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%DataScience%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'ana_lopez' AND p.content LIKE '%DataScience%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%MERN%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%MERN%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'maria_garcia' AND p.content LIKE '%ProductManagement%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '1 day' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, u.id, p.id
FROM users u, posts p
WHERE u.username = 'carlos_rodriguez' AND p.content LIKE '%ProductManagement%'
    AND NOT EXISTS (SELECT 1 FROM reactions r WHERE r.user_id = u.id AND r.post_id = p.id);

-- =====================================================
-- INSERTAR SEGUIMIENTOS DE PRUEBA
-- =====================================================

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '27 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, maria.id, carlos.id
FROM users maria, users carlos WHERE maria.username = 'maria_garcia' AND carlos.username = 'carlos_rodriguez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '26 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, maria.id, ana.id
FROM users maria, users ana WHERE maria.username = 'maria_garcia' AND ana.username = 'ana_lopez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, maria.id, laura.id
FROM users maria, users laura WHERE maria.username = 'maria_garcia' AND laura.username = 'laura_sanchez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carlos.id, maria.id
FROM users carlos, users maria WHERE carlos.username = 'carlos_rodriguez' AND maria.username = 'maria_garcia'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carlos.id, david.id
FROM users carlos, users david WHERE carlos.username = 'carlos_rodriguez' AND david.username = 'david_martinez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '20 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carlos.id, javier.id
FROM users carlos, users javier WHERE carlos.username = 'carlos_rodriguez' AND javier.username = 'javier_fernandez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, ana.id, maria.id
FROM users ana, users maria WHERE ana.username = 'ana_lopez' AND maria.username = 'maria_garcia'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, ana.id, laura.id
FROM users ana, users laura WHERE ana.username = 'ana_lopez' AND laura.username = 'laura_sanchez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '13 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, david.id, carlos.id
FROM users david, users carlos WHERE david.username = 'david_martinez' AND carlos.username = 'carlos_rodriguez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '12 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, david.id, javier.id
FROM users david, users javier WHERE david.username = 'david_martinez' AND javier.username = 'javier_fernandez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '8 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, laura.id, maria.id
FROM users laura, users maria WHERE laura.username = 'laura_sanchez' AND maria.username = 'maria_garcia'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, laura.id, ana.id
FROM users laura, users ana WHERE laura.username = 'laura_sanchez' AND ana.username = 'ana_lopez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '6 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, javier.id, carlos.id
FROM users javier, users carlos WHERE javier.username = 'javier_fernandez' AND carlos.username = 'carlos_rodriguez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, javier.id, david.id
FROM users javier, users david WHERE javier.username = 'javier_fernandez' AND david.username = 'david_martinez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carmen.id, maria.id
FROM users carmen, users maria WHERE carmen.username = 'carmen_ruiz' AND maria.username = 'maria_garcia'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

INSERT INTO follows (follow_date, follower_id, followed_id)
SELECT CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carmen.id, laura.id
FROM users carmen, users laura WHERE carmen.username = 'carmen_ruiz' AND laura.username = 'laura_sanchez'
ON CONFLICT (follower_id, followed_id) DO NOTHING;

-- =====================================================
-- INSERTAR COMENTARIOS DE PRUEBA
-- =====================================================

-- Comentarios: cada target recibe un comentario de su actor asignado
INSERT INTO comments (content, creation_date, update_date, author_id, post_id)
SELECT
    CONCAT('Comentario de ', actor.username, ' sobre tu publicación'),
    CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval,
    CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval,
    actor.id,
    p.id
FROM users actor
JOIN users target ON TRUE
JOIN posts p ON p.author_id = target.id
WHERE (actor.username = 'carlos_rodriguez' AND target.username = 'maria_garcia')
     OR (actor.username = 'ana_lopez' AND target.username = 'carlos_rodriguez')
     OR (actor.username = 'david_martinez' AND target.username = 'ana_lopez')
     OR (actor.username = 'laura_sanchez' AND target.username = 'david_martinez')
     OR (actor.username = 'javier_fernandez' AND target.username = 'laura_sanchez')
     OR (actor.username = 'carmen_ruiz' AND target.username = 'javier_fernandez')
     OR (actor.username = 'maria_garcia' AND target.username = 'carmen_ruiz')
    AND p.creation_date = (
        SELECT MAX(creation_date) FROM posts WHERE author_id = target.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM comments c WHERE c.author_id = actor.id AND c.post_id = p.id
    );

-- Reacciones: cada actor da LIKE al post más reciente del target
INSERT INTO reactions (type, creation_date, user_id, post_id)
SELECT 'LIKE', CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, actor.id, p.id
FROM users actor
JOIN users target ON TRUE
JOIN posts p ON p.author_id = target.id
WHERE (actor.username = 'carlos_rodriguez' AND target.username = 'maria_garcia')
     OR (actor.username = 'ana_lopez' AND target.username = 'carlos_rodriguez')
     OR (actor.username = 'david_martinez' AND target.username = 'ana_lopez')
     OR (actor.username = 'laura_sanchez' AND target.username = 'david_martinez')
     OR (actor.username = 'javier_fernandez' AND target.username = 'laura_sanchez')
     OR (actor.username = 'carmen_ruiz' AND target.username = 'javier_fernandez')
     OR (actor.username = 'maria_garcia' AND target.username = 'carmen_ruiz')
    AND p.creation_date = (
        SELECT MAX(creation_date) FROM posts WHERE author_id = target.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM reactions r WHERE r.user_id = actor.id AND r.post_id = p.id
    )
    ON CONFLICT (user_id, post_id) DO NOTHING;

-- Notificaciones: avisar al target sobre la interacción (referencia al post)
INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT
    'Nuevo comentario',
    actor.first_name || ' ' || actor.last_name || ' ha comentado en tu publicación',
    'COMMENT',
    false,
    CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval,
    NULL,
    p.id,
    'posts',
    target.id
FROM users actor
JOIN users target ON TRUE
JOIN posts p ON p.author_id = target.id
WHERE (actor.username = 'carlos_rodriguez' AND target.username = 'maria_garcia')
     OR (actor.username = 'ana_lopez' AND target.username = 'carlos_rodriguez')
     OR (actor.username = 'david_martinez' AND target.username = 'ana_lopez')
     OR (actor.username = 'laura_sanchez' AND target.username = 'david_martinez')
     OR (actor.username = 'javier_fernandez' AND target.username = 'laura_sanchez')
     OR (actor.username = 'carmen_ruiz' AND target.username = 'javier_fernandez')
     OR (actor.username = 'maria_garcia' AND target.username = 'carmen_ruiz')
    AND p.creation_date = (
        SELECT MAX(creation_date) FROM posts WHERE author_id = target.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM notifications n WHERE n.user_id = target.id AND n.reference_table = 'posts' AND n.reference_id = p.id
    );

-- Reportes: crear reportes individuales con timestamps únicos
-- Reporte 1: carlos_rodriguez reporta a maria_garcia
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '2 hours' - INTERVAL '15 minutes' - INTERVAL '30 seconds',
    NULL, carlos.id, p.id
FROM users carlos, users maria, posts p 
WHERE carlos.username = 'carlos_rodriguez' AND maria.username = 'maria_garcia' AND p.author_id = maria.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 2: carlos_rodriguez reporta otro post de maria_garcia
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '5 hours' - INTERVAL '42 minutes' - INTERVAL '18 seconds',
    NULL, carlos.id, p.id
FROM users carlos, users maria, posts p 
WHERE carlos.username = 'carlos_rodriguez' AND maria.username = 'maria_garcia' AND p.author_id = maria.id
ORDER BY p.creation_date DESC LIMIT 1 OFFSET 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 3: ana_lopez reporta a carlos_rodriguez
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '8 hours' - INTERVAL '23 minutes' - INTERVAL '45 seconds',
    NULL, ana.id, p.id
FROM users ana, users carlos, posts p 
WHERE ana.username = 'ana_lopez' AND carlos.username = 'carlos_rodriguez' AND p.author_id = carlos.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 4: david_martinez reporta a ana_lopez
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '11 hours' - INTERVAL '8 minutes' - INTERVAL '52 seconds',
    NULL, david.id, p.id
FROM users david, users ana, posts p 
WHERE david.username = 'david_martinez' AND ana.username = 'ana_lopez' AND p.author_id = ana.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 5: laura_sanchez reporta a david_martinez
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '14 hours' - INTERVAL '35 minutes' - INTERVAL '12 seconds',
    NULL, laura.id, p.id
FROM users laura, users david, posts p 
WHERE laura.username = 'laura_sanchez' AND david.username = 'david_martinez' AND p.author_id = david.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 6: carmen_ruiz reporta a javier_fernandez
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '17 hours' - INTERVAL '51 minutes' - INTERVAL '5 seconds',
    NULL, carmen.id, p.id
FROM users carmen, users javier, posts p 
WHERE carmen.username = 'carmen_ruiz' AND javier.username = 'javier_fernandez' AND p.author_id = javier.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 7: javier_fernandez reporta a laura_sanchez
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '4 days' - INTERVAL '3 hours' - INTERVAL '19 minutes' - INTERVAL '37 seconds',
    NULL, javier.id, p.id
FROM users javier, users laura, posts p 
WHERE javier.username = 'javier_fernandez' AND laura.username = 'laura_sanchez' AND p.author_id = laura.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 8: ana_lopez reporta otro post
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '4 days' - INTERVAL '6 hours' - INTERVAL '44 minutes' - INTERVAL '21 seconds',
    NULL, ana.id, p.id
FROM users ana, users carlos, posts p 
WHERE ana.username = 'ana_lopez' AND carlos.username = 'carlos_rodriguez' AND p.author_id = carlos.id
ORDER BY p.creation_date DESC LIMIT 1 OFFSET 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 9: maria_garcia reporta a carmen_ruiz
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Revisión solicitada', 'Se solicita revisión por contenido potencialmente inapropiado', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '4 days' - INTERVAL '9 hours' - INTERVAL '27 minutes' - INTERVAL '49 seconds',
    NULL, maria.id, p.id
FROM users maria, users carmen, posts p 
WHERE maria.username = 'maria_garcia' AND carmen.username = 'carmen_ruiz' AND p.author_id = carmen.id
ORDER BY p.creation_date DESC LIMIT 1
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- =====================================================
-- INSERTAR NOTIFICACIONES DE PRUEBA
-- =====================================================

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo usuario registrado', 'Carlos Rodriguez se ha unido a MindHub', 'ADMIN_ACTION', false, CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '25 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, carlos.id, 'users', admin.id
FROM users admin, users carlos WHERE admin.username = 'admin' AND carlos.username = 'carlos_rodriguez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = admin.id AND reference_table = 'users' AND reference_id = carlos.id AND message LIKE '%Carlos Rodriguez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo reporte', 'Se ha reportado un post por contenido inapropiado', 'ADMIN_ACTION', false, CURRENT_TIMESTAMP - INTERVAL '10 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'reports', admin.id
FROM users admin WHERE admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = admin.id AND reference_table = 'reports' AND reference_id = 1 AND title = 'Nuevo reporte');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '28 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '27 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', maria.id
FROM users maria, users admin WHERE maria.username = 'maria_garcia' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = maria.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Carlos Rodriguez ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '26 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', maria.id
FROM users maria WHERE maria.username = 'maria_garcia'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = maria.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Carlos Rodriguez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nueva reacción', 'Ana Lopez ha reaccionado a tu publicación', 'REACTION', false, CURRENT_TIMESTAMP - INTERVAL '24 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', maria.id
FROM users maria WHERE maria.username = 'maria_garcia'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = maria.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Ana Lopez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '23 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '22 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', carlos.id
FROM users carlos, users admin WHERE carlos.username = 'carlos_rodriguez' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = carlos.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Maria Garcia ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '21 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', carlos.id
FROM users carlos WHERE carlos.username = 'carlos_rodriguez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = carlos.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Maria Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nueva reacción', 'David Martinez ha reaccionado a tu publicación', 'REACTION', false, CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', carlos.id
FROM users carlos WHERE carlos.username = 'carlos_rodriguez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = carlos.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%David Martinez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '19 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '18 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', ana.id
FROM users ana, users admin WHERE ana.username = 'ana_lopez' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = ana.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Maria Garcia ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '17 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', ana.id
FROM users ana WHERE ana.username = 'ana_lopez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = ana.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Maria Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '14 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '13 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', david.id
FROM users david, users admin WHERE david.username = 'david_martinez' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = david.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Carlos Rodriguez ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '12 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', david.id
FROM users david WHERE david.username = 'david_martinez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = david.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Carlos Rodriguez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '9 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '8 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', laura.id
FROM users laura, users admin WHERE laura.username = 'laura_sanchez' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = laura.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Maria Garcia ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', laura.id
FROM users laura WHERE laura.username = 'laura_sanchez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = laura.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Maria Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '6 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', javier.id
FROM users javier, users admin WHERE javier.username = 'javier_fernandez' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = javier.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Carlos Rodriguez ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', javier.id
FROM users javier WHERE javier.username = 'javier_fernandez'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = javier.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Carlos Rodriguez%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo seguidor', 'Juan Carlos Garcia ha comenzado a seguirte', 'FOLLOW', true, CURRENT_TIMESTAMP - INTERVAL '4 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, admin.id, 'users', carmen.id
FROM users carmen, users admin WHERE carmen.username = 'carmen_ruiz' AND admin.username = 'admin'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = carmen.id AND reference_table = 'users' AND reference_id = admin.id AND message LIKE '%Juan Carlos Garcia%');

INSERT INTO notifications (title, message, type, read, creation_date, read_date, reference_id, reference_table, user_id)
SELECT 'Nuevo comentario', 'Maria Garcia ha comentado en tu publicación', 'COMMENT', false, CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, NULL, 1, 'posts', carmen.id
FROM users carmen WHERE carmen.username = 'carmen_ruiz'
    AND NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = carmen.id AND reference_table = 'posts' AND reference_id = 1 AND message LIKE '%Maria Garcia%');

-- =====================================================
-- INSERTAR REPORTES DE PRUEBA
-- =====================================================

-- Reporte 10: maria_garcia reporta contenido inapropiado
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Contenido inapropiado', 'Este post contiene lenguaje ofensivo y no es apropiado para la comunidad', 'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '10 days' - INTERVAL '4 hours' - INTERVAL '12 minutes' - INTERVAL '33 seconds',
    NULL, maria.id, p.id
FROM users maria, posts p WHERE maria.username = 'maria_garcia' AND p.content LIKE '%Bienvenidos a MindHub%'
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 11: carlos_rodriguez reporta spam (RESOLVED)
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Spam', 'Este usuario esta publicando contenido promocional excesivo', 'RESOLVED',
    CURRENT_TIMESTAMP - INTERVAL '15 days' - INTERVAL '7 hours' - INTERVAL '38 minutes' - INTERVAL '55 seconds',
    CURRENT_TIMESTAMP - INTERVAL '5 days' - INTERVAL '10 hours' - INTERVAL '22 minutes' - INTERVAL '14 seconds',
    carlos.id, p.id
FROM users carlos, posts p WHERE carlos.username = 'carlos_rodriguez' AND p.content LIKE '%WebSockets%'
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- Reporte 12: ana_lopez reporta información falsa (REJECTED)
INSERT INTO reports (reason, description, status, report_date, review_date, reporter_id, post_id)
SELECT 'Informacion falsa', 'El contenido de este post contiene informacion tecnica incorrecta', 'REJECTED',
    CURRENT_TIMESTAMP - INTERVAL '8 days' - INTERVAL '13 hours' - INTERVAL '47 minutes' - INTERVAL '8 seconds',
    CURRENT_TIMESTAMP - INTERVAL '3 days' - INTERVAL '16 hours' - INTERVAL '29 minutes' - INTERVAL '41 seconds',
    ana.id, p.id
FROM users ana, posts p WHERE ana.username = 'ana_lopez' AND p.content LIKE '%Angular 20%'
ON CONFLICT (reporter_id, post_id) DO NOTHING;

-- =====================================================
-- VERIFICAR QUE EXISTA EL CHATBOT ANTES DE INSERTAR MENSAJES
-- =====================================================

-- Verificar si existe el chatbot, si no, crearlo
DO $$
BEGIN
    -- Crear chatbot si no existe
    IF NOT EXISTS (SELECT 1 FROM chatbots WHERE name = 'Asistente Virtual FCT') THEN
        INSERT INTO chatbots (name, description, model, active, creation_date, update_date) VALUES
        ('Asistente Virtual FCT', 'Intelligent chatbot for social network. Provides support, answers questions, and helps users navigate the platform. Specialized in FCT (Formacion en Centros de Trabajo) topics and general social media assistance.', 'Predefined Intelligent System', true, CURRENT_DATE, CURRENT_DATE);
    END IF;
END $$;

-- =====================================================
-- INSERTAR MENSAJES DEL CHATBOT DE PRUEBA
-- =====================================================

INSERT INTO chatbot_messages (content, type, creation_date, session_id, user_id, chatbot_id)
SELECT 'Como puedo cambiar mi configuracion de privacidad?', 'USER', CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 'session_maria_001', maria.id, cb.id
FROM users maria, chatbots cb WHERE maria.username = 'maria_garcia' AND cb.name = 'Asistente Virtual FCT';

INSERT INTO chatbot_messages (content, type, creation_date, session_id, user_id, chatbot_id)
SELECT 'Para cambiar tu configuracion de privacidad, ve a tu perfil, haz clic en "Configuracion" y selecciona "Privacidad". Alli podras elegir entre publico y privado.', 'CHATBOT', CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 'session_maria_001', maria.id, cb.id
FROM users maria, chatbots cb WHERE maria.username = 'maria_garcia' AND cb.name = 'Asistente Virtual FCT';

INSERT INTO chatbot_messages (content, type, creation_date, session_id, user_id, chatbot_id)
SELECT 'Puedo eliminar un comentario que hice?', 'USER', CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 'session_carlos_001', carlos.id, cb.id
FROM users carlos, chatbots cb WHERE carlos.username = 'carlos_rodriguez' AND cb.name = 'Asistente Virtual FCT';

INSERT INTO chatbot_messages (content, type, creation_date, session_id, user_id, chatbot_id)
SELECT 'Si, puedes eliminar tus propios comentarios. Ve al comentario y haz clic en los tres puntos (...) para ver las opciones disponibles.', 'CHATBOT', CURRENT_TIMESTAMP - INTERVAL '2 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 'session_carlos_001', carlos.id, cb.id
FROM users carlos, chatbots cb WHERE carlos.username = 'carlos_rodriguez' AND cb.name = 'Asistente Virtual FCT';

-- =====================================================
-- INSERTAR ACCIONES ADMINISTRATIVAS DE PRUEBA
-- =====================================================

-- Reporte 11 resuelto: carlos reportó post de luis (con WebSockets)
INSERT INTO admin_actions (action, description, reason, action_date, object_id, object_table, admin_id, affected_user_id, affected_username, affected_first_name, affected_last_name)
SELECT 'RESOLVE_REPORT', 'Reporte resolved - Razón: Spam - Comentario del admin: Contenido promocional excesivo removido', 'Reporte resuelto y publicación eliminada', CURRENT_TIMESTAMP - INTERVAL '5 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 11, 'reports', admin.id, luis.id, luis.username, luis.first_name, luis.last_name
FROM users admin, users luis WHERE admin.username = 'admin' AND luis.username = 'luis_martinez';

-- Reporte 12 rechazado: ana reportó post de laura
INSERT INTO admin_actions (action, description, reason, action_date, object_id, object_table, admin_id, affected_user_id, affected_username, affected_first_name, affected_last_name)
SELECT 'REJECT_REPORT', 'Reporte rejected - Razón: Informacion falsa - Comentario del admin: Contenido verificado como correcto', 'Reporte rechazado', CURRENT_TIMESTAMP - INTERVAL '3 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, 12, 'reports', admin.id, laura.id, laura.username, laura.first_name, laura.last_name
FROM users admin, users laura WHERE admin.username = 'admin' AND laura.username = 'laura_sanchez';

-- Admin actualizó perfil de maria_garcia
INSERT INTO admin_actions (action, description, reason, action_date, object_id, object_table, admin_id, affected_user_id, affected_username, affected_first_name, affected_last_name)
SELECT 'UPDATE_USER', 'Perfil de usuario actualizado', 'Se actualizo la informacion del perfil del usuario', CURRENT_TIMESTAMP - INTERVAL '7 days' - (floor(random()*24)::int::text || ' hours')::interval - (floor(random()*60)::int::text || ' minutes')::interval, maria.id, 'users', admin.id, maria.id, maria.username, maria.first_name, maria.last_name
FROM users admin, users maria WHERE admin.username = 'admin' AND maria.username = 'maria_garcia';

-- =====================================================
-- AGREGAR IMÁGENES DE PERFIL ÚNICAS A TODOS LOS USUARIOS
-- =====================================================

-- Admin (hombre ejecutivo con traje)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150&h=150&fit=crop&crop=face'
WHERE username = 'admin';

-- Maria (mujer desarrolladora joven)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150&h=150&fit=crop&crop=face'
WHERE username = 'maria_garcia';

-- Carlos (hombre desarrollador con gafas)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop&crop=face'
WHERE username = 'carlos_rodriguez';

-- Ana (mujer diseñadora creativa)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1548142813-c348350df52b?w=150&h=150&fit=crop&crop=face'
WHERE username = 'ana_lopez';

-- David (hombre DevOps con barba)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face'
WHERE username = 'david_martinez';

-- Laura (mujer científica de datos)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face'
WHERE username = 'laura_sanchez';

-- Javier (hombre full stack joven)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face'
WHERE username = 'javier_fernandez';

-- Carmen (mujer product manager profesional)
UPDATE users 
SET profile_picture = 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=150&h=150&fit=crop&crop=face'
WHERE username = 'carmen_ruiz';
