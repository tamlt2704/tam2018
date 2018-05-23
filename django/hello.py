import os
import sys

from django.conf import settings

settings.configure(
	DEBUG=True,
	ROOT_URLCONF=__name__,
	MIDDLEWARE_CLASSES=(
		'django.middleware.common.CommonMiddleware',
		'django.middleware.csrf.CsrfViewMiddleware',
		'django.middleware.clickjacking.XFrameOptionsMiddleware',
	),
)

from django.conf.urls import url
from django.http import HttpResponse
from django.core.wsgi import get_wsgi_application

application = get_wsgi_application()

def index(request):
	return HttpResponse('django')

urlpatterns = (
	url(r'^$', index),
)

if __name__ == "__main__":
	from django.core.management import execute_from_command_line
	execute_from_command_line(sys.argv)

