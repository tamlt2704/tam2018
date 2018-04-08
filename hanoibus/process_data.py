import pandas as pd
import json
import os


def process_geo():
    busgeo_df = pd.read_csv('static/BusGeo.csv', index_col=[0])

    for k, g in busgeo_df.groupby(['Code', 'Direction']):
        js = g.sort_values('GeoSequence')[['Lat', 'Lng']]
        js = js.to_dict()
        filename = 'static/geo_{}_{}.json'.format(k[0], k[1])
        with open(filename, 'w') as f:
            json.dump(js, f)


def process_bus_stop_code():
    df = pd.read_csv('static/BusStopCode.csv', index_col=[0])
    df = df[['BusStopCode', 'FleetOver', 'Name']]
    df = df.set_index('BusStopCode')
    js = df.T.to_dict('dict')

    filename = 'static/busstops.json'
    with open(filename, 'w') as f:
        json.dump(js, f, ensure_ascii=False)

def process_bus_route():
    bus_stop_code_df = pd.read_csv('static/BusStopCode.csv', index_col=[0])

    df = pd.read_csv('static/BusRoute.csv', index_col=[0])
    for k, g in df.groupby(['Code', 'Direction']):
        js = g.sort_values('StopSequence')[['BusStopCode']]
        js = pd.merge(js, bus_stop_code_df, left_on='BusStopCode', right_on='BusStopCode', how='left')
        js = js[['BusStopCode', 'FleetOver', 'Name', 'Lat', 'Lng']]
        js = js.to_dict()
        filename = 'static/route_{}_{}.json'.format(k[0], k[1])
        with open(filename, 'w') as f:
            json.dump(js, f)


# def process_bus_info():
d = {}
for fn in os.listdir('data/raw_data'):
    if fn.startswith('fleed_'):
        try:
            with open('data/raw_data/{}'.format(fn), 'r') as f:
                for line in f:
                    js = json.loads(line)
                    js = json.loads(js)

                    title = js['dt']['Data'][0]['Name']
                    bus_service_no = title.split('-')[0].strip()
                    print '"' + bus_service_no +'": "' + '-'.join([x.strip() for x in title.split('-')[1:]]) + '",'
        except:
            pass
