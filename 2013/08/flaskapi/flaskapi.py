from flask import abort, jsonify, Flask, request
from flask.views import MethodView


class APIView(MethodView):
    """ Generic API, all the API views will inherit for this base class
        every dispatch method will return an invalid request message, every
        child class must implements this methods properly
        More info about flask class based views here
        http://flask.pocoo.org/docs/views/#method-based-dispatching.
    """

    ENDPOINT = '/mymusic/api/v1.0'

    def get(self):
        abort(400)

    def post(self):
        abort(400)

    def put(self):
        abort(400)

    def delete(self):
        abort(400)

    def json_response(self, data={}):
        return jsonify(data)


class UserResource(object):
    def __init__(self, data={}, model=None, **kwargs):
        self._dict_data = data
        self._is_valid = True

    def is_valid(self):
        return self._is_valid

    def to_serializable_dict(self):
        return self._dict_data

    def add(self):
        for key, value in self._dict_data.items():
            setattr(self, key, value)

    def update(self):
        for key, value in self._dict_data.items():
            setattr(self, key, value)

    def delete(self):
        self._dict_data = {}

    @classmethod
    def get(cls, user_id):
        dict_data = {'id': user_id, 'name': 'user {0}'.format(user_id)}
        return UserResource(data=dict_data)

    @classmethod
    def get_list(cls, *args, **kwargs):
        user_list = []
        for x in range(4):
            dict_data = {'id': x, 'name': 'user {0}'.format(x)}
            resource = UserResource(data=dict_data)
            user_list.append(resource.to_serializable_dict())

        return user_list



class UserAPIView(APIView):
    def get(self, user_id):
        """
        If GET request have the id: attribute, this view will search and return
        an user with this id, If id: atrribute is no setted will return list of
        users
        """
        if user_id:
            user_resource = UserResource.get(user_id)
            data_dict = {'user': user_resource.to_serializable_dict()}
        else:
            data_dict = {'users': UserResource.get_list()}
        return self.json_response(data=data_dict)


    def post(self):
        """
        user Creation is performed by POST METHOD
        """
        response = {}
        user_resource = UserResource(request.json)
        if user_resource.is_valid():
            try:
                user_resource.add()
                response['user'] = user_resource.to_serializable_dict()
            except Exception as error:
                pass
        return self.json_response(data=response)

    def put(self, user_id):
        """
        User modification using PUT method
        """
        response = {}
        user_resource = UserResource(request.json)
        if user_resource.is_valid():
            try:
                user_resource.update()
                response['user'] = user_resource.to_serializable_dict()
            except Exception as error:
                pass
        return self.json_response(data=response)

    def delete(self, user_id):
        response = {}
        user_resource = UserResource(request.json)
        try:
            user_resource.delete()
            response['ok'] = 'record deleted'
        except Exception as error:
            pass
        return self.json_response(data=response)


app = Flask(__name__)
user_view = UserAPIView.as_view('user_api')
app.add_url_rule('{0}/users/'.format(UserAPIView.ENDPOINT), defaults={'user_id': None},
                  view_func=user_view, methods=['GET',])

app.add_url_rule('{0}/users/'.format(UserAPIView.ENDPOINT), view_func=user_view, methods=['POST',])

app.add_url_rule('{0}/users/<int:user_id>'.format(UserAPIView.ENDPOINT), view_func=user_view,
                 methods=['GET', 'PUT', 'DELETE'])

def run_app():
    app.run()

if __name__ == "__main__":
    run_app()


