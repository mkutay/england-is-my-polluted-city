# WE GET THESE 100S (VOL 2)

This project implements a fully functional JavaFX Application to display historical Air Pollution Data of the
whole United Kingdom on an interactive map. Users are able to explore the historic pollution levels between
2018 and 2023 on a colour coded map, as well as view detailed location and pollution data at specific data
points. There are many quality of life features implemented, such as a robust GUI to filter through different
years and pollutants, an extensive statistics framework, as well as a “colourblind mode” to make the
pollution viewer more accessible.

[Google Docs](https://docs.google.com/document/d/19Ut53-j0HDo7lLMJCrqWUXvecwEDDJxhTycay316bNM/edit?usp=sharing)

## On commit messages

- Be as descriptive as possible
- Create your commits like your Java classes: cohesive. When you change some *functionality*, commit to the repo.
- Commits should only serve one purpose.
- [Some conventions for commit messages](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716).

## External Libraries and APIs Used 

- [Gluon Maps](https://github.com/gluonhq/maps) - Maps Library by Gluon (maintainer of JavaFX) that implements OpenStreetMaps
- [OSGB by DST](https://github.com/dstl/osgb]) - Library to convert British Grid System (Easting / Northing) to Latitude and Longitude 
- [GeographicLib](https://github.com/geographiclib/geographiclib-java) - Used for geodesic distance calculation
- [Postcodes.io](https://postcodes.io/) - API used to get location & address data from the specified longitude and latitude on the map
- [GSON](https://github.com/google/gson) - A library by Google to convert between Json and Java Objects
- [World Air Quality Index API](https://aqicn.org/api/) - An API to get real time Air Quality Index Updates from the World Air Quality Index Project

## Authors

Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, Chelsea Feliciano
