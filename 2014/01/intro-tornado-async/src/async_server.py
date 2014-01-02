import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web

from tornado import gen

from models import AsyncUser

class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", UserHandler),
        ]
        tornado.web.Application.__init__(self, handlers)


class UserHandler(tornado.web.RequestHandler):

    @gen.coroutine
    def get(self):
        user = AsyncUser()
        response = yield (user.save())

        response2 = yield (user.send_email())
        response3 = yield (user.social_api())
        self.finish()

def main():
    http_server = tornado.httpserver.HTTPServer(Application())
    PORT = 8001
    print("serving at port", PORT)
    http_server.listen(PORT)
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    main()
