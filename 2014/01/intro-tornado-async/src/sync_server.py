from models import User

from datetime import datetime

import http.server
import socketserver


class SyncServer(http.server.BaseHTTPRequestHandler):
    def do_GET(self, *args, **kwargs):
        user = User()
        response = user.save()
        response1 = user.send_email()
        response2 = user.social_api()
        print(response)
        print(response2)
        self.send_response(200, message='ok')
        self.end_headers()


def run():
    PORT = 8000

    Handler = SyncServer

    httpd = socketserver.TCPServer(("", PORT), Handler)

    print("serving at port", PORT)
    httpd.serve_forever()


if __name__ == '__main__':
    run()
