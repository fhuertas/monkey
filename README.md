# Monkey Problem

[![Build Status](https://travis-ci.org/fhuertas/monkey.svg?branch=master)](https://travis-ci.org/fhuertas/monkey)
[![Coverage Status](https://coveralls.io/repos/github/fhuertas/monkey/badge.svg?branch=master)](https://coveralls.io/github/fhuertas/monkey?branch=master)

## Description

### English 

We want to know if African monkeys can be taught about deadlocks. She locates a deep canyon and
fastens a rope across it, so the monkeys can cross hand-over-hand.

Passage along the rope follows these rules:

- Several monkeys can cross at the same time, provided that they are all going in the same
direction.
- If eastward moving and westward moving monkeys ever get onto the rope at the same time, a
deadlock will result (the monkeys will get stuck in the middle) because it is impossible for one
monkey to climb over another one while suspended over the canyon.
- If a monkey wants to cross the canyon, he must check to see that no other monkey is currently
crossing in the opposite direction.
- Your solution should avoid starvation. When a monkey that wants to cross to the east arrives at
the rope and finds monkeys crossing to the west, the monkey waits until the rope in empty, but
no more westward moving monkeys are allowed to start until at least one monkey has crossed
the other way.

For this exercise, you are to write a program to simulate activity for this canyon crossing problem:

- Simulate each monkey as a separate process.
- Altogether, a lot of monkeys, whatever you want, will cross the canyon, with a random number
generator specifying whether they are eastward moving or westward moving.
- Use a random number generator, so the time between monkey arrivals is between 1 and 8
seconds.
- Each monkey takes 1 second to get on the rope. (That is, the minimum inter-monkey spacing is
1 second.)
- All monkeys travel at the same speed. Each traversal takes exactly 4 seconds, after the monkey
is on the rope.

Enjoy it!

### Spanish

Queremos saber si los monos africanos pueden ser enseñados sobre los deadlocks. Hay un cañón profundo y una cuerda que lo atraviesa (Este - Oeste), por esta cuerda los monios pueden cruzar utilizando sus manos

La reglas para poder cruzar son las siguintes:

- Varios monos pueden cruzar al mismo tiempo, siempre y cuando todos vayan en la misma dirección.
- Si los monos que se mueven hacia el este y hacia el oeste llegan a la cuerda al mismo tiempo, se producirá un bloqueo (los monos quedarán atascados en el medio) porque es imposible que un mono monte sobre otro mientras está suspendido sobre el cañón.
- Si un mono quiere cruzar el cañón, debe comprobar para ver que ningún otro mono está cruzando actualmente en la dirección opuesta.
- Su solución debe evitar el hambre. Cuando un mono que quiera cruzar al este llega a la cuerda, si encuentra monos que cruzan al oeste, el mono espera hasta que la cuerda esté vacía, no se permite que crucen más monos hacia el este hasta que al menos uno haya cruzado en el otro sentido.

Para este ejercicio, debe escribir un programa para simular actividad para este problema de cruce de cañones:

- Simular cada mono como un proceso separado.
- Simular con un conjunto finito de monos, la dirección de cada uno se genera aleatoriamente. 
- Cada mono tarda en llegar entre 1 y 8 segundos. En conjunto, un montón de monos, lo que quieras, va a cruzar el cañón, con un generador de números aleatorios que especifican si están en movimiento hacia el este o en dirección oeste.
- Cada mono tarda en subir a la cuerda 1 segundo. 
- Todos los monos tardan lo mismo en cruzar la cuerda, 4 segundos

## Run a monkey simulation

### Requirements. 

You need have installed sbt in your machine (with its dependencies). 

### Configuration parameters

This simulation uses typesafe config to take the parameters. You can set the configuration file with 
the flag `-Dconfig.file`. The configuration file by default is application.conf (or reference.conf, if other doesn't
exist) included in resources folder (src/main/resource). 

The configuration parameters are the following:

* **monkey.waiting_min**: The minimum time (milliseconds) time that a monkey is waiting after it hasn't been 
allowed to cross. The default value is 200
* **monkey.waiting_max**: The maximum time (milliseconds) time that a monkey is waiting after it hasn't been 
allowed to cross. The default value is 100
* **monkey.number**: The number of monkey. The default by value is 8 
* **monkey.min_time**: The minimum time (milliseconds) between monkeys. The default value is 8000
* **monkey.max_time**: The minimum time (milliseconds) between monkeys. The default value is 1000
* **monkey.cross_time**: The time it takes for a monkey to cross the canyon. The default value is 4000  
* **monkey.rope_time**: The time it takes for a monkey to climb to robe. The default value is 1000 

### Run simulation

To run the simulation you should download the repository. After it, you should run `sbt run` command, 
you can add a custom application.conf before run the simulation.

All the options to run the project are the following:

* `sbt run`: Run the simulation.
* `sbt compile`: Compile the project
* `sbt test`: Run the tests
* `sbt clean coverage test && sbt coverageReport`: To test the application and show the coverage

