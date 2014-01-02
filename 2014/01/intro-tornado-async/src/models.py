import time
import datetime
from tornado.concurrent import return_future

class User(object):
    def save(self):
        time.sleep(0.02)
        return datetime.datetime.utcnow()

    def send_email(self):
        time.sleep(0.06)
        return datetime.datetime.utcnow()

    def social_api(self):
        time.sleep(0.2)
        return datetime.datetime.utcnow()


class AsyncUser(object):
    @return_future
    def save(self, callback=None):
        time.sleep(0.02)
        result = datetime.datetime.utcnow()
        callback(result)

    @return_future
    def send_email(self, callback=None):
        time.sleep(0.06)
        result = datetime.datetime.utcnow()
        callback(result)

    @return_future
    def social_api(self, callback=None):
        time.sleep(0.2)
        result = datetime.datetime.utcnow()
        callback(result)
