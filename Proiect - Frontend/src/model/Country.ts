import { City } from "./City";

export interface Country {
    id: number;
    name: string;
    population: number;
    area: number;
    continent: string;
    capital: City;
    cities: City[];
    code: string;
}
