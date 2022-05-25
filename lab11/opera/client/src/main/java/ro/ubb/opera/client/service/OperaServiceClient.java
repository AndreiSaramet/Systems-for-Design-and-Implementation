package ro.ubb.opera.client.service;

import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.common.domain.Opera;
import ro.ubb.opera.common.service.OperaService;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class OperaServiceClient implements OperaService {
    private final ExecutorService executorService;

    private final TcpClient tcpClient;

    public OperaServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Opera> addOpera(String title, String language, Integer composerId) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.ADD_OPERA, title + TOKENS_DELIMITER + language + TOKENS_DELIMITER + composerId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OperaService.decodeOpera(response.getBody());
        });
    }

    @Override
    public Future<Opera> findOperaById(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.FIND_OPERA_BY_ID, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OperaService.decodeOpera(response.getBody());
        });
    }

    @Override
    public Future<Opera> findOperaByTitle(String title) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.FIND_OPERA_BY_TITLE, title);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OperaService.decodeOpera(response.getBody());
        });
    }

    @Override
    public Future<Set<Opera>> findOperasByLanguage(String language) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.FIND_OPERAS_BY_LANGUAGE, language);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeOperaSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Opera>> findOperasByComposer(Integer composerId) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.FIND_OPERAS_BY_COMPOSER, composerId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeOperaSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Opera>> getAllOperas() {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.GET_ALL_OPERA, "");

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeOperaSet(response.getBody());
        });
    }

    @Override
    public Future<Opera> updateOpera(Integer id, String title, String language, Integer composerId) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.UPDATE_OPERA, id.toString() + Service.TOKENS_DELIMITER + title + Service.TOKENS_DELIMITER + language + Service.TOKENS_DELIMITER + composerId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OperaService.decodeOpera(response.getBody());
        });
    }

    @Override
    public Future<Opera> deleteOpera(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(OperaService.DELETE_OPERA, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OperaService.decodeOpera(response.getBody());
        });
    }

    private Set<Opera> decodeOperaSet(String encodedOperaSet) {
        Set<String> tokens = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(encodedOperaSet, Service.ENTITIES_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens.stream().map(OperaService::decodeOpera).collect(Collectors.toSet());
    }
}
