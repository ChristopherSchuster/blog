import feedparser
import httplib2
import os
import shutil
import xml.etree.ElementTree as ET


def get_container_list():
    """
    Get container list from open data Colombia
    """
    h = httplib2.Http()
    (resp_headers, content) = (
        h.request("http://servicedatosabiertoscolombia.cloudapp.net/v1/",
        "GET"))
    root = ET.fromstring(content)
    return [ child.get('href') for child in root[0]]


def get_catalog_list(container_list):
    """
    Get Catalog list
    """
    h = httplib2.Http()
    url = "http://servicedatosabiertoscolombia.cloudapp.net/v1/"
    catalogs_by_entity = {}

    for container in container_list:
        catalog_list = []
        if container is not None:
            (resp_headers, content) = h.request(url + container, "GET")
            try:
                root = ET.fromstring(content)
                for child in root[0]:
                    if 'href' in child.attrib:
                        catalog_list.append(child.attrib['href'])
            except Exception as e:
                print e
                print '\n'
                print container
            catalogs_by_entity[container] = catalog_list
    return catalogs_by_entity

def get_catalogs(catalogs_by_entity, directory='./'):
    """
    Get catalog data and save it in disk
    """
    h = httplib2.Http()
    root_path = os.path.join(directory, 'opendata')
    # remove directory if exist
    if os.path.exists(root_path):
        shutil.rmtree(root_path)
    os.mkdir(root_path)
    url = "http://servicedatosabiertoscolombia.cloudapp.net/v1/"
    for k, v in catalogs_by_entity.items():
        dir_path = os.path.join(root_path, k)
        os.makedirs(dir_path)
        for cat in v:
            (resp_headers, content) = h.request(
                url + k + '/' + cat + '?$format=json', "GET")
            fname = cat + '.json'
            f = open (os.path.join(dir_path, fname), 'w')
            f.write(content)
            f.close()


def main():
    cointainer_list = get_container_list()
    catalogs_by_entity = get_catalog_list(cointainer_list)
    get_catalogs(catalogs_by_entity)

if __name__ == "__main__":
    main()
