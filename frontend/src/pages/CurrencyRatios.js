import React from "react";
const config = require("../config.js");

function formatDate(date) {
    let year = date.getFullYear();
    let month = ("0"+(date.getMonth()+1)).slice(-2);
    let day = ("0" + date.getDate()).slice(-2);

    return day + "_" + month + "_" + year;
}

function formatJSON(today,yesterday) {
    let array = [];

    console.log(today);
    console.log(yesterday);

    let i = 0;

    today['_embedded']['ratioList'].map(item => {
        let object = {
            "currency": item['currencyUnit']['currencyName'],
            "today": item['ratio'],
            "id": item['id'],
            "scale": item['currencyUnit']['currencyScale']
        };
        array.push(object);
    })
    i = 0;

    yesterday['_embedded']['ratioList'].map(item => {
        array[i]['yesterday'] = item['ratio'];
        array[i]['difference'] = item['ratio'] - array[i]['today'];
        array[i]['differencePercentage'] = (array[i]['difference'] / array[i]['today']) * 100;
        array[i]['difference'] > 0 ? array[i]['positive'] = true : array[i]['positive'] = false;
        i++;
    })

    array.pop();
    return array;
}

function printItem(item) {
    var type;
    if(item.positive) {
        type = 'positive-ratio';
    } else {
        type = 'negative-ratio';
    }
    return (
        <tr>
            <th>{item.scale} {item.currency}</th>
            <td>{item.today}</td>
            <td className={type}>{item.difference.toFixed(4)}</td>
            <td className={type}>{item.differencePercentage.toFixed(4)}%</td>
        </tr>
    );
}

class CurrencyRatios extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoadedToday: false,
            isLoadedYesterday: false,
            today: {},
            yesterday: {}
        };
    }

    componentDidMount() {
        let date = new Date();
        date.setDate(date.getDate() - 1);

        fetch("/ratios")
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    this.setState({
                        isLoadedToday: true,
                        today: result,
                    });
                },
                (error) => {
                    this.setState({
                        isLoadedToday: true,
                        error
                    });
                }
            )

        fetch("/ratios?" + new URLSearchParams({ondate: formatDate(date)}))
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    this.setState({
                        isLoadedYesterday: true,
                        yesterday: result,
                    });
                },
                (error) => {
                    this.setState({
                        isLoadedYesterday: true,
                        error
                    });
                }
            )
    }

    render() {
        const { error, isLoadedToday, isLoadedYesterday, today, yesterday } = this.state;
        if(error) {
            return <h1>Ошибка: {error.message}</h1>;
        } else if(!(isLoadedToday && isLoadedYesterday)) {
            return <h1>Загрузка...</h1>;
        } else {
            let ratios = formatJSON(today,yesterday);
            return (
                <div className="container">
                <div className="col-md-auto">
                    <h1 className="mt-3 mb-3">Курсы валют</h1>
                    <table className="table table-bordered">
                        <thead>
                            <tr>
                                <th>Валюта</th>
                                <th>Сегодня</th>
                                <th>Разница</th>
                                <th>Разница процентов</th>
                            </tr>
                        </thead>
                        <tbody>
                        {ratios.map(item => (
                        printItem(item)
                        ))}
                        </tbody>
                    </table>
                </div>
                </div>
            );
        }

    }

}

export default CurrencyRatios;