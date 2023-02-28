import { Button, TextField } from "@mui/material";
import { Box } from "@mui/system"
import axios from "axios";
import { FC, useState } from "react"
import { Country } from "../model/Country";

export type AddCityFormProps = {
    country: Country;
    reloadCountry: () => void;
}

const AddCityForm: FC<AddCityFormProps> = ({ country, reloadCountry }) => {
    const [cityName, setCityName] = useState<string>();

    const addCity = () => {
        axios.post("http://localhost:8080/countries/" + country.id + "/cities", { name: cityName }).then(response => {
            setCityName("");
            reloadCountry();
        });
    }

    return <Box>
        <TextField label="City name" value={cityName} onChange={(e) => setCityName(e.target.value)}></TextField>
        <Button onClick={() => addCity()}>Add</Button>
    </Box>;
}

export default AddCityForm;