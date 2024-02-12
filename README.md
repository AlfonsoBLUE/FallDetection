# TFG MARÍA DEL SER GUTIÉRREZ. 
# Detección de caídas usando el ecosistema de sensores integrados en un smartwatch con WEAR OS.

Este proyecto se ha desarrollado como Trabajo de Fin de Grado para el grado de Ingeniería Informática (mención Ingeniería de Software) de la Universidad de Valladolid por la estudiante MAría del Ser Gutiérrez.

El proyecto consta de una aplicación Android para dispositivos con sistema operativo WearOS cuyo objetivo es la detección de caídas de los usuarios que portan estos dispositivos. Este proyecto se ha realizado como complemento al proyecto de monitorización realizado por el estudiante Sergio Sanz Sanz.

La aplicación final cuenta con las siguientes características:

 - Monitorización de la ubicación.
 - Monitorización de la retirada del dispositivo.
 - Monitorización de la caída del usuario.
 - Determinación del tipo de caída (leve: usuario consciente; grave: usuario inconsciente; falsa: el usuario no se ha caído).
 - Notificación al servidor (centro de control) de todos los eventos previamente mencionados.
 - Descarga de configuración desde el servidor.
 - Pausa de la monitorización de caídas en caso de retirada del dispositivo.
 - Modo desarrollador para poder recoger datos de los algoritmos de detección de caídas.

El contenido de este proyecto se encuentra en la ruta: app > src > main > java > com > example > tfg_smartwatch

Para poder utilizar la aplicación se debe clonar el repositorio completo, generar el respectivo APK e instalarlo en el smartwatch. Es necesario que el servidor (externo a este proyecto) se encuentre en funcionamiento para una correcta ejecución.

___

## Modos de ejecución del proyecto.

Antes de ejecutar el proyecto es necesario consultar el archivo: app > src > main > java > com > example > tfg_smartwatch > Constantes.java

Este cuenta con los umbrales para la detección de caídas y retirada mediante acelerómetro. Además, se ha de seleccionar en él qué modo de ejecución se quiere utilizar. En concreto, hay que revisar las siguientes tres constantes:


* La app va a tener dos modos de monitorizar la ubicacion (MODO_MONITORIZACION_UBICACION):
    * Modo 1: Alta prioridad, el tiempo que se establece en el intervalo es el que se cumple en la monitorizacon.
    * Modo 2: Ahorro de bateria, se prioriza el ahorro de batería pero se pierde precision en la ubicacion y en el intervalo de monitorizacion.
* La app va a tener tres modos de monitorizar la caida (MODO_MONITORIZACION_CAIDA):
    * Modo 1: Monitorizacion mediante angulos, los umbrales a utilizar para determinar si se ha dado una caida son GSVM y el Angulo.
    * Modo 2: Monitorizacion mediante angulos, los umbrales a utilizar para determinar si se ha dado una caida son SVM y el Angulo.
    * Modo 3: Monitorizacion mediante golpe posterior, los umbrales a utilizar para determinar si se ha dado una caida son SVM y el Impacto.
* La app va a tener el modo de ejecucion en debug (MODO_DEBUG):
    * False: Modo de utilizacion para usuarios. No se guardan datos recogidos por los sensores.
    * True: Modo de utilizacion para desarrolladores. Se guardan (y muestran por pantalla) datos recogidos por los sensores.
