1. Initial project
	+ configure django
	+ create url patterns
	+ def func with HttpResponse
	+ execute_from_command_line

2. Server django as production ready 
	from django.core.wsgi import get_wsgi_application
	application = get_wsgi_application()

	pip install gunicorn
	gunicorn hello --log-file=-

3. Additional configurations
	DEBUG, ALLOWED_HOST, SECRET_KEY

4. create template folder
	django-admin startproject foo --template=project_name
