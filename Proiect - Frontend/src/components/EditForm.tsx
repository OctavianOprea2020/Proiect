import { Button, Card, CardActions, CardContent, TextField, Typography } from "@mui/material";
import { Box } from "@mui/system"
import axios from "axios";
import { FC, useState } from "react"
import { Country } from "../model/Country";

export type EditFormProps = {
    selectedCountry: Country;
    value?: number;
    closeEditForm: (reload: boolean) => void;
    reloadCountry: () => void;
}

const getFormat = (value: Number ): string => {
    return value.toLocaleString()
  } 

const EditForm: FC<EditFormProps> = ({ selectedCountry, value, closeEditForm, reloadCountry }) => {
    const [oldPopulation, setOldPopulation] = useState<number>(selectedCountry.population);
    const [capitalName, setCapitalName] = useState<string>(selectedCountry.capital.name);
    const [population, setPopulation] = useState<number>(selectedCountry.population);

    const save = () => {
        axios.patch("http://localhost:8080/countries/" + selectedCountry.id, { capital: capitalName, diffPopulation: population - oldPopulation }).then(response => reloadCountry());
    }

    const increase = () => {
        setPopulation(oldPopulation + 1);
        setOldPopulation(oldPopulation + 1);
        axios.patch("http://localhost:8080/countries/" + selectedCountry.id, { capital: selectedCountry.capital.name, diffPopulation: 1 })
            .then(response => reloadCountry());
    }

    const decrease = () => {
        setPopulation(oldPopulation - 1);
        setOldPopulation(oldPopulation - 1);
        axios.patch("http://localhost:8080/countries/" + selectedCountry.id, { capital: selectedCountry.capital.name, diffPopulation: -1 })
            .then(response => reloadCountry());
    }

    return <Box>
        <Card>
            <CardContent>
                <Typography>{selectedCountry?.name} ({selectedCountry?.code})</Typography>
                <TextField sx={{ margin: 1 }} label="Capital name" value={selectedCountry.capital.name} onChange={(e) => setCapitalName(e.target.value)}></TextField>
                <TextField sx={{ margin: 1 }} label="Population" value={getFormat(selectedCountry?.population)} onChange={(e) => setPopulation(e.target.value as unknown as number)}></TextField>
            </CardContent>
            <CardActions>
                <Button variant="contained" onClick={() => save()}>Save</Button>
                <Button onClick={() => increase()}>Increase population</Button>
                <Button onClick={() => decrease()}>Decrease population</Button>
            </CardActions>
        </Card>
    </Box>
}

export default EditForm;