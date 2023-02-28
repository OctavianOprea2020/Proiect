import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import { Country } from './model/Country';
import { Box, Button, Card, CardContent, FormControl, Grid, InputLabel, MenuItem, Pagination, Select, TextField, Typography } from '@mui/material';
import LearnEditForm from './components/LearnEditForm';
import EditForm from './components/EditForm';
import AddCityForm from './components/AddCityForm';


function App() {
  const [page, setPage] = useState(1);
  const [countries, setCountries] = useState<Country[]>();
  const [selectedCountry, setSelectedCountry] = useState<Country>();
  const [searchText, setSearchText] = useState<string>("");
  const [continent, setContinent] = useState<string | null>(null);
  const [routefrom, setRouteFrom] = useState<string | null>(null);
  const [routeto, setRouteTo] = useState<string | null>(null);
  const [routeCountries, setRouteCountries] = useState<string>("");
  const [selectedRoute, setSelectedRoute] = useState<string[]>([]);
  
  useEffect(() => {
    axios.get('http://localhost:8080/countries').then((response) => setCountries(response.data));
  }, []);

  const reloadCountries = () => {
    axios.get('http://localhost:8080/countries?searchText=' + searchText + (continent ? '&continent=' + continent : "")).then((response) => setCountries(response.data));
  }

  useEffect(() => {
    reloadCountries();
    setPage(1);
    setSelectedCountry(undefined);
  }, [searchText, continent]);

  useEffect(() => {
    if (countries && Math.ceil(countries.length / 10) < page) {
      setPage(page - 1);
    }
  }, [countries]);

  const onCountryClicked = (id: number) => {
    axios.get('http://localhost:8080/countries/' + id).then((response) => setSelectedCountry(response.data));
  }

  const reloadSelectedCountry = () => {
    selectedCountry && onCountryClicked(selectedCountry.id);
  }

  const getFormat = (value: Number ): string => {
    return value.toLocaleString()
  } 

  const deleteCountry = (id: number) => {
    axios.delete('http://localhost:8080/countries/' + id).then((response) => {
      setSelectedCountry(undefined);
      reloadCountries()
    });
  }

  const clearSelectedCountry = () => {
    setSelectedCountry(undefined);
  }

  const getCountryFlagUrl = (name: string): string => {
    //return "https://www.geonames.org/flags/x/" + name.substring(0, 2).toLowerCase() + ".gif";
    return "https://www.geonames.org/flags/x/" + getCountryID(name) + ".gif";
  }

  const isContinentEurope = (name: string | undefined): boolean => {
    if (name !== undefined) {
      if (name.toLowerCase() == "europe") 
        return true;
      else
        return false;
    } else
    return false;
}

const getCountryID = (name: string | undefined): string => {
    if (name !== undefined) {
      switch (name.toLowerCase()) 
      {
        case "albania":
          return "al";
        case "andorra":
          return "ad";
        case "austria":
          return "at";
        case "belarus":
          return "by";
        case "belgium":
          return "be";
        case "bosnia and herzegovina":
          return "ba";
        case "bulgaria":
          return "bg";
        case "croatia":
          return "hr";
        case "cyprus":
          return "cy";
        case "czechia":
          return "cz";
        case "denmark":
          return "dk";
        case "estonia":
          return "ee";
        case "finland":
          return "fi";
        case "france":
          return "fr";
        case "germany":
          return "de";
        case "greece":
          return "gr";
        case "hungary":
          return "hu";
        case "iceland":
          return "is";
        case "ireland":
          return "ie";
        case "italy":
          return "it";
        case "latvia":
          return "lv";
        case "liechtenstein":
          return "li";
        case "lithuania":
          return "lt";
        case "luxembourg":
          return "lu";
        case "malta":
          return "mt";
        case "moldova":
          return "md";
        case "monaco":
          return "mc";
        case "montenegro":
          return "me";
        case "netherlands":
          return "nl";
        case "north macedonia":
          return "mk";
        case "norway":
          return "no";
        case "poland":
          return "pl";
        case "portugal":
          return "pt";
        case "romania":
          return "ro";
        case "russia":
          return "ru";
        case "san marino":
          return "sm";
        case "serbia":
          return "rs";
        case "slovakia":
          return "sk";
        case "slovenia":
          return "si";
        case "spain":
          return "es";
        case "sweden":
          return "se";
        case "switzerland":
          return "ch";
        case "turkey":
          return "tr";
        case "ukraine":
          return "ua";
        case "united kingdom":
          return "uk";
        case "vatican":
          return "va";
        case "afghanistan":
          return "af";
        case "algeria":
          return "dz";
        case "american samoa":
          return "ws";
        case "angola":
          return "ao";
        case "anguilla":
          return "ai";
        case "antigua and barbuda":
          return "ag";
        case "argentina":
          return "ar";
        case "armenia":
          return "am";
        case "aruba":
          return "aw";
        case "australia":
          return "au";
        case "azerbaijan":
          return "az";
        case "bahamas":
          return "bs";
        case "bahrain":
          return "bh";
        case "bangladesh":
          return "bd";
        case "barbados":
          return "bb";
        case "belize":
          return "bz";
        case "benin":
          return "bj";
        case "bermuda":
          return "bm";
        case "bhutan":
          return "bt";
        case "bolivia (plurinational state of)":
          return "bo";
        case "botswana":
          return "bw";
        case "brazil":
          return "br";
        case "brunei darussalam":
          return "bn";
        case "burkina faso":
          return "bf";
        case "burundi":
          return "bi";
        case "cabo verde":
          return "cv";
        case "cambodia":
          return "kh";
        case "cameroon":
          return "cm";
        case "canada":
          return "ca";
        case "cayman islands":
          return "ky";
        case "central african republic":
          return "cf";
        case "chad":
          return "td";
        case "chile":
          return "cl";
        case "china":
          return "cn";
        case "christmas island":
          return "cx";
        case "cocos (keeling) islands":
          return "cc";
        case "colombia":
          return "co";
        case "comoros":
          return "km";
        case "congo":
          return "cm";
        case "congo (democratic republic of the)":
          return "cd";
        case "cook islands":
          return "ck";
        case "costa rica":
          return "cr";
        case "côte d'ivoire":
          return "ci";
        case "cuba":
          return "cu";
        case "curaçao":
          return "cw";
        case "djibouti":
          return "dj";
        case "dominica":
          return "dm";
        case "dominican republic":
          return "do";
        case "ecuador":
          return "ec";
        case "egypt":
          return "eg";
        case "el salvador":
          return "sv";
        case "equatorial guinea":
          return "gq";
        case "eritrea":
          return "er";
        case "ethiopia":
          return "et";
        case "falkland islands (malvinas)":
          return "fk";
        case "fiji":
          return "fj";
        case "french polynesia":
          return "pf";
        case "french southern territories":
          return "tf";
        case "gabon":
          return "ga";
        case "gambia":
          return "gm";
        case "georgia":
          return "ge";
        case "ghana":
          return "gh";
        case "greenland":
          return "gl";
        case "grenada":
          return "gd";
        case "guam":
          return "gu";
        case "Guatemala":
          return "gt";
        case "guinea":
          return "gn";
        case "guinea-bissau":
          return "gw";
        case "guyana":
          return "gy";
        case "haiti":
          return "ht";
        case "honduras":
          return "hn";
        case "hong kong":
          return "hk";
        case "india":
          return "in";
        case "indonesia":
          return "id";
        case "iran (islamic republic of)":
          return "ir";
        case "iraq":
          return "iq";
        case "israel":
          return "il";
        case "jamaica":
          return "jm";
        case "japan":
          return "jp";
        case "jordan":
          return "jo";
        case "kazakhstan":
          return "kz";
        case "kenya":
          return "ke";
        case "kiribati":
          return "ki";
        case "korea (democratic people's republic of)":
          return "kp";
        case "korea (republic of)":
          return "kr";
        case "kuwait":
          return "kw";
        case "kyrgyzstan":
          return "kg";
        case "lao people's democratic republic":
          return "la";
        case "lebanon":
          return "lb";
        case "lesotho":
          return "ls";
        case "liberia":
          return "lr";
        case "libya":
          return "ly";
        case "madagascar":
          return "mg";
        case "malawi":
          return "mw";
        case "malaysia":
          return "my";
        case "maldives":
          return "mv";
        case "mali":
          return "ml";
        case "marshall islands":
          return "mh";
        case "mauritania":
          return "mr";
        case "mauritius":
          return "mu";
        case "mexico":
          return "mx";
        case "micronesia (federated states of)":
          return "fm";
        case "mongolia":
          return "mn";
        case "montserrat":
          return "ms";
        case "morocco":
          return "ma";
        case "mozambique":
          return "mz";
        case "myanmar":
          return "mm";
        case "namibia":
          return "na";
        case "nauru":
          return "nr";
        case "nepal":
          return "np";
        case "new caledonia":
          return "nc";
        case "new zealand":
          return "nz";
        case "nicaragua":
          return "ni";
        case "niger":
          return "ne";
        case "nigeria":
          return "ng";
        case "niue":
          return "nu";
        case "norfolk island":
          return "nf";
        case "northern mariana islands":
          return "mp";
        case "oman":
          return "om";
        case "pakistan":
          return "pk";
        case "palau":
          return "pw";
        case "panama":
          return "pa";
        case "papua new guinea":
          return "pg";
        case "paraguay":
          return "py";
        case "peru":
          return "pe";
        case "philippines":
          return "ph";
        case "pitcairn":
          return "pn";
        case "puerto rico":
          return "pr";
        case "qatar":
          return "qa";
        case "rwanda":
          return "rw";
        case "saint barthélemy":
          return "bl";
        case "saint kitts and nevis":
          return "kn";
        case "saint lucia":
          return "lc";
        case "saint martin (french part)":
          return "mf";
        case "saint pierre and miquelon":
          return "pm";
        case "saint vincent and the grenadines":
          return "vc";
        case "samoa":
          return "ws";
        case "sao tome and principe":
          return "st";
        case "saudi arabia":
          return "sa";
        case "senegal":
          return "sn";
        case "seychelles":
          return "sc";
        case "sierra leone":
          return "sl";
        case "singapore":
          return "sg";
        case "sint maarten (dutch part)":
          return "sx";
        case "solomon islands":
          return "sb";
        case "somalia":
          return "so";
        case "south africa":
          return "za";
        case "south sudan":
          return "ss";
        case "sri lanka":
          return "lk";
        case "sudan":
          return "sd";
        case "suriname":
          return "sr";
        case "Swaziland":
          return "sz";
        case "syrian arab republic":
          return "sy";
        case "taiwan":
          return "tw";
        case "tajikistan":
          return "tj";
        case "tanzania, united republic of":
          return "tz";
        case "thailand":
          return "th";
        case "timor-leste":
          return "tl";
        case "togo":
          return "tg";
        case "tokelau":
          return "tk";
        case "tonga":
          return "to";
        case "trinidad and tobago":
          return "tt";
        case "tunisia":
          return "tn";
        case "turkmenistan":
          return "tm";
        case "turks and caicos islands":
          return "tc";
        case "tuvalu":
          return "tv";
        case "uganda":
          return "ug";
        case "united arab emirates":
          return "ae";
        case "united states of america":
          return "us";
        case "uruguay":
          return "uy";
        case "uzbekistan":
          return "uz";
        case "vanuatu":
          return "vu";
        case "venezuela (bolivarian republic of)":
          return "ve";
        case "viet nam":
          return "vn";
        case "virgin islands (british)":
          return "vg";
        case "virgin islands (u.s.)":
          return "vi";
        case "wallis and futuna":
          return "wf";
        case "western sahara":
          return "eh";
        case "yemen":
          return "ye";
        case "zambia":
          return "zm";
        case "zimbabwe":
          return "zw";
        default:
          return name.substring(0, 2).toLowerCase();
      }
    } else
        return "";
  }
  
  const fromChild = (text: string) => {
    console.log("something from child " + text);
  }

  const closeEditForm = (reload: boolean) => {
    setSelectedCountry(undefined);
    if (reload) {
      reloadCountries();
    }
  }

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  }

  return (
    <Box className="App" sx={{ backgroundColor: 'lightgray', height: 1, display: "flex", flexDirection: "column" }}>
      <Box sx={{ width: 1, display: "flex" }}>
        <img src='https://www.freepnglogos.com/uploads/globe-png/globe-icon-aquanox-icons-softiconsm-34.png' width="100" height="100"></img>
        <Box sx={{ flexGrow: 1 }}></Box>
        
        <FormControl sx={{ margin: 1, width: '120px' }}>
          <InputLabel>Continent: </InputLabel>
          <Select
            value={continent}
            label="Continent"
            onChange={(e) => setContinent(e.target.value)}
          >
            <MenuItem value="Europe">Europe</MenuItem>
            <MenuItem value="Oceania">Oceania</MenuItem>
            <MenuItem value="Asia">Asia</MenuItem>
            <MenuItem value="Africa">Africa</MenuItem>
            <MenuItem value="Americas">Americas</MenuItem>
            <MenuItem value="Antarctica">Antarctica</MenuItem>
          </Select>
        </FormControl>

        <TextField sx={{ margin: 1, mr: 2, width: '100px' }} label="Search:" value={searchText} onChange={(e) => setSearchText(e.target.value)}></TextField>

        <FormControl disabled={!isContinentEurope(continent?.valueOf())} sx={{ margin: 1, width: '190px' }}>
          <InputLabel>Route From: </InputLabel>
          <Select
            value={routefrom}
            label="routefrom"
            onChange={(e) => setRouteFrom(e.target.value)}
          >
            <MenuItem value="Albania">Albania</MenuItem>
            <MenuItem value="Andorra">Andorra</MenuItem>
            <MenuItem value="Austria">Austria</MenuItem>
            <MenuItem value="Belarus">Belarus</MenuItem>
            <MenuItem value="Belgium">Belgium</MenuItem>
            <MenuItem value="Bosnia and Herzegovina">Bosnia and Herzegovina</MenuItem>
            <MenuItem value="Bulgaria">Bulgaria</MenuItem>
            <MenuItem value="Croatia">Croatia</MenuItem>
            <MenuItem value="Cyprus">Cyprus</MenuItem>
            <MenuItem value="Czechia">Czechia</MenuItem>
            <MenuItem value="Denmark">Denmark</MenuItem>
            <MenuItem value="Estonia">Estonia</MenuItem>
            <MenuItem value="Finland">Finland</MenuItem>
            <MenuItem value="France">France</MenuItem>
            <MenuItem value="Germany">Germany</MenuItem>
            <MenuItem value="Greece">Greece</MenuItem>
            <MenuItem value="Hungary">Hungary</MenuItem>
            <MenuItem value="Iceland">Iceland</MenuItem>
            <MenuItem value="Ireland">Ireland</MenuItem>
            <MenuItem value="Italy">Italy</MenuItem>
            <MenuItem value="Latvia">Latvia</MenuItem>
            <MenuItem value="Liechtenstein">Liechtenstein</MenuItem>
            <MenuItem value="Lithuania">Lithuania</MenuItem>
            <MenuItem value="Luxembourg">Luxembourg</MenuItem>
            <MenuItem value="Malta">Malta</MenuItem>
            <MenuItem value="Moldova">Moldova</MenuItem>
            <MenuItem value="Monaco">Monaco</MenuItem>
            <MenuItem value="Montenegro">Montenegro</MenuItem>
            <MenuItem value="Netherlands">Netherlands</MenuItem>
            <MenuItem value="North Macedonia">North Macedonia</MenuItem>
            <MenuItem value="Norway">Norway</MenuItem>
            <MenuItem value="Poland">Poland</MenuItem>
            <MenuItem value="Portugal">Portugal</MenuItem>
            <MenuItem value="Romania">Romania</MenuItem>
            <MenuItem value="Russia">Russia</MenuItem>
            <MenuItem value="San Marino">San Marino</MenuItem>
            <MenuItem value="Serbia">Serbia</MenuItem>
            <MenuItem value="Slovakia">Slovakia</MenuItem>
            <MenuItem value="Slovenia">Slovenia</MenuItem>
            <MenuItem value="Spain">Spain</MenuItem>
            <MenuItem value="Sweden">Sweden</MenuItem>
            <MenuItem value="Switzerland">Switzerland</MenuItem>
            <MenuItem value="Turkey">Turkey</MenuItem>
            <MenuItem value="Ukraine">Ukraine</MenuItem>
            <MenuItem value="United Kingdom">United Kingdom</MenuItem>
            <MenuItem value="Vatican">Vatican</MenuItem>
          </Select>
          </FormControl>

          <FormControl disabled={!isContinentEurope(continent?.valueOf())} sx={{ margin: 1, width: '190px' }}>
          <InputLabel>Route To: </InputLabel>
          <Select
            value={routeto}
            label="routeto"
            onChange={(e) => setRouteTo(e.target.value)}
          >
            <MenuItem value="Albania">Albania</MenuItem>
            <MenuItem value="Andorra">Andorra</MenuItem>
            <MenuItem value="Austria">Austria</MenuItem>
            <MenuItem value="Belarus">Belarus</MenuItem>
            <MenuItem value="Belgium">Belgium</MenuItem>
            <MenuItem value="Bosnia and Herzegovina">Bosnia and Herzegovina</MenuItem>
            <MenuItem value="Bulgaria">Bulgaria</MenuItem>
            <MenuItem value="Croatia">Croatia</MenuItem>
            <MenuItem value="Cyprus">Cyprus</MenuItem>
            <MenuItem value="Czechia">Czechia</MenuItem>
            <MenuItem value="Denmark">Denmark</MenuItem>
            <MenuItem value="Estonia">Estonia</MenuItem>
            <MenuItem value="Finland">Finland</MenuItem>
            <MenuItem value="France">France</MenuItem>
            <MenuItem value="Germany">Germany</MenuItem>
            <MenuItem value="Greece">Greece</MenuItem>
            <MenuItem value="Hungary">Hungary</MenuItem>
            <MenuItem value="Iceland">Iceland</MenuItem>
            <MenuItem value="Ireland">Ireland</MenuItem>
            <MenuItem value="Italy">Italy</MenuItem>
            <MenuItem value="Latvia">Latvia</MenuItem>
            <MenuItem value="Liechtenstein">Liechtenstein</MenuItem>
            <MenuItem value="Lithuania">Lithuania</MenuItem>
            <MenuItem value="Luxembourg">Luxembourg</MenuItem>
            <MenuItem value="Malta">Malta</MenuItem>
            <MenuItem value="Moldova">Moldova</MenuItem>
            <MenuItem value="Monaco">Monaco</MenuItem>
            <MenuItem value="Montenegro">Montenegro</MenuItem>
            <MenuItem value="Netherlands">Netherlands</MenuItem>
            <MenuItem value="North Macedonia">North Macedonia</MenuItem>
            <MenuItem value="Norway">Norway</MenuItem>
            <MenuItem value="Poland">Poland</MenuItem>
            <MenuItem value="Portugal">Portugal</MenuItem>
            <MenuItem value="Romania">Romania</MenuItem>
            <MenuItem value="Russia">Russia</MenuItem>
            <MenuItem value="San Marino">San Marino</MenuItem>
            <MenuItem value="Serbia">Serbia</MenuItem>
            <MenuItem value="Slovakia">Slovakia</MenuItem>
            <MenuItem value="Slovenia">Slovenia</MenuItem>
            <MenuItem value="Spain">Spain</MenuItem>
            <MenuItem value="Sweden">Sweden</MenuItem>
            <MenuItem value="Switzerland">Switzerland</MenuItem>
            <MenuItem value="Turkey">Turkey</MenuItem>
            <MenuItem value="Ukraine">Ukraine</MenuItem>
            <MenuItem value="United Kingdom">United Kingdom</MenuItem>
            <MenuItem value="Vatican">Vatican</MenuItem>
          </Select>
        </FormControl>
        <FormControl>
          <Button disabled={!isContinentEurope(continent?.valueOf())} sx={{ color: "red" }} onClick={() => 
            {
              {/*axios.get("http://localhost:8080/countries/getRoute/" + getCountryID(routefrom?.valueOf()) + "/" + getCountryID(routeto?.valueOf())).then((response) => {
                setRouteCountries(response.data);
              })*/}
              axios.get("http://localhost:8080/countries/getExtendedRoute/" + getCountryID(routefrom?.valueOf()) + "/" + getCountryID(routeto?.valueOf())).then((response) => {
                const routes = response.data.split(';')
                console.log(routes); 
                setSelectedRoute(routes);
              })
          }}>Find Route</Button>
        </FormControl>
        <FormControl sx={{ color: "blue", width: '400px'}}>
          {/*<TextField disabled={!isContinentEurope(continent?.valueOf())} sx={{ margin: 1, mr: 2, width: '300px' }} label="Route:" value={routeCountries}></TextField>*/}
          <ol>
            {selectedRoute.map(route => <li>{route}</li>)}
          </ol>
        </FormControl>

      </Box>
      {/* <LearnEditForm text='first' callParent={fromChild}></LearnEditForm> */}
      {/* <LearnEditForm text='second' callParent={fromChild}></LearnEditForm> */}
      {selectedCountry &&
        <Grid container spacing={0}>
          <Grid item xs={6}>
            <Card sx={{ margin: 1, overflow: "unset" }}>
              <Button onClick={() => clearSelectedCountry()}>Close</Button>
              <Button sx={{ color: "red" }} onClick={() => deleteCountry(selectedCountry.id)}>Delete</Button>
              <Typography fontSize="small">Population: {getFormat(selectedCountry.population)}</Typography>
              <Typography fontSize="small">Area: {getFormat(selectedCountry.area)}</Typography>
              <Typography fontSize="small">Continent: {selectedCountry.continent}</Typography>
              <Typography fontSize="small">Number of cities: {selectedCountry.cities.length}</Typography>
              <img width="300px"  height="190" src={getCountryFlagUrl(selectedCountry.name)}></img>
            </Card>
          </Grid>
          <Grid item xs={6}>
            <Box sx={{ margin: 1 }}>
              <EditForm closeEditForm={closeEditForm} selectedCountry={selectedCountry} reloadCountry={reloadSelectedCountry}></EditForm>
              <Card sx={{ mt: 2 }}>
                <CardContent>Adding Cities
                  <AddCityForm reloadCountry={reloadSelectedCountry} country={selectedCountry}></AddCityForm>
                  {selectedCountry.cities.map(city => <Typography>{city.name}</Typography>)}
                </CardContent>
              </Card>
            </Box>
          </Grid>
        </Grid>}

      <Pagination onChange={handlePageChange} count={countries ? Math.ceil(countries.length / 10) : 0} page={page}></Pagination>
      <Box sx={{ overflow: "auto" }}>
        {countries?.slice((page - 1) * 10, page * 10).map(country =>
          <Card sx={{ margin: 2, cursor: "pointer" }} onClick={() => onCountryClicked(country.id)}>
            <CardContent>
              {country.name}
            </CardContent>
          </Card>
        )}
      </Box>
    </Box>
  );
}

export default App;
